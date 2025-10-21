package org.pcfx.node.androidn1.domain

import android.util.Log
import org.pcfx.node.androidn1.model.ExposureEvent
import org.pcfx.node.androidn1.model.KnowledgeAtom
import java.time.Instant
import java.util.UUID
import kotlin.math.min

interface Atomizer {
    suspend fun process(events: List<ExposureEvent>): List<KnowledgeAtom>
}

class DefaultAtomizer(
    private val ner: NER = RuleBasedNER(),
    private val tone: ToneClassifier = RuleBasedToneClassifier(),
    private val vectorizer: Vectorizer = HashedBagOfWordsVectorizer()
) : Atomizer {
    override suspend fun process(events: List<ExposureEvent>): List<KnowledgeAtom> {
        return events.mapNotNull { event ->
            try {
                atomizeEvent(event)
            } catch (e: Exception) {
                Log.e(TAG, "Error atomizing event ${event.id}", e)
                null
            }
        }
    }

    private suspend fun atomizeEvent(event: ExposureEvent): KnowledgeAtom {
        val text = event.content.text ?: ""
        require(text.isNotBlank()) { "Event text is required for atomization" }

        val entities = ner.extract(text)
        val toneScores = tone.classify(text)
        val vectorRef = vectorizer.vectorize(text)
        val confidence = calculateConfidence(text, entities)

        return KnowledgeAtom.builder()
            .id(generateAtomId(event.id))
            .ts(Instant.now().toString())
            .provenance(
                KnowledgeAtom.Provenance(
                    eventId = event.id,
                    adapterId = event.adapterId,
                    analysisNodeId = "pcfx.atomizer.android-n1/0.1.0"
                )
            )
            .text(text)
            .entities(entities)
            .tone(toneScores)
            .vectorRef(vectorRef)
            .confidence(confidence)
            .build()
    }

    private fun generateAtomId(eventId: String): String {
        return UUID.nameUUIDFromBytes("$eventId:atomize".toByteArray()).toString()
    }

    private fun calculateConfidence(text: String, entities: List<KnowledgeAtom.Entity>): Map<String, Double> {
        val extractionConfidence = when {
            text.length > 50 && entities.isNotEmpty() -> 0.92
            text.length > 20 && entities.isNotEmpty() -> 0.85
            text.length > 10 -> 0.75
            else -> 0.6
        }

        return mapOf(
            "extraction" to extractionConfidence,
            "verifiability" to 0.3
        )
    }

    companion object {
        private const val TAG = "DefaultAtomizer"
    }
}

interface NER {
    fun extract(text: String): List<KnowledgeAtom.Entity>
}

class RuleBasedNER : NER {
    private val brandPattern = Regex("""(?:Brand\s+[A-Z]\w*|\b[A-Z]{2,}\b)""")
    private val handlePattern = Regex("""@(\w+)""")
    private val hashtagPattern = Regex("""#(\w+)""")
    private val urlPattern = Regex("""https?://[^\s]+""")

    override fun extract(text: String): List<KnowledgeAtom.Entity> {
        val entities = mutableListOf<KnowledgeAtom.Entity>()

        // Extract brands
        brandPattern.findAll(text).forEach { match ->
            entities.add(
                KnowledgeAtom.Entity(
                    id = match.value.replace(Regex("""\s+"""), ""),
                    type = "org",
                    name = match.value
                )
            )
        }

        // Extract handles
        handlePattern.findAll(text).forEach { match ->
            entities.add(
                KnowledgeAtom.Entity(
                    id = match.groupValues[1],
                    type = "person",
                    name = "@${match.groupValues[1]}"
                )
            )
        }

        // Extract hashtags
        hashtagPattern.findAll(text).forEach { match ->
            entities.add(
                KnowledgeAtom.Entity(
                    id = match.groupValues[1],
                    type = "topic",
                    name = "#${match.groupValues[1]}"
                )
            )
        }

        // Extract URLs
        urlPattern.findAll(text).forEach { match ->
            entities.add(
                KnowledgeAtom.Entity(
                    id = match.value.take(20),
                    type = "url",
                    name = match.value
                )
            )
        }

        // Deduplicate by ID
        return entities.distinctBy { it.id }
    }
}

interface ToneClassifier {
    fun classify(text: String): Map<String, Double>
}

class RuleBasedToneClassifier : ToneClassifier {
    private val aspirationKeywords = listOf("amazing", "incredible", "best", "love", "dream", "aspire", "success", "achieve")
    private val authorityKeywords = listOf("expert", "leader", "authority", "powerful", "control", "dominant", "influence")
    private val fearKeywords = listOf("dangerous", "threat", "risk", "afraid", "danger", "scary", "warning")
    private val scarcityKeywords = listOf("limited", "rare", "exclusive", "few", "last", "urgent", "now", "quickly")
    private val guiltKeywords = listOf("should", "must", "responsible", "guilty", "shame", "wrong")
    private val curiosityKeywords = listOf("wonder", "discover", "explore", "interesting", "curious", "mystery", "secret")
    private val socialProofKeywords = listOf("everyone", "popular", "trending", "viral", "love", "favorite", "best")

    override fun classify(text: String): Map<String, Double> {
        val lowerText = text.lowercase()
        val words = lowerText.split(Regex("""\W+"""))

        return mapOf(
            "aspiration" to calculateScore(words, aspirationKeywords),
            "authority" to calculateScore(words, authorityKeywords),
            "fear" to calculateScore(words, fearKeywords),
            "scarcity" to calculateScore(words, scarcityKeywords),
            "guilt" to calculateScore(words, guiltKeywords),
            "curiosity" to calculateScore(words, curiosityKeywords),
            "social_proof" to calculateScore(words, socialProofKeywords)
        )
    }

    private fun calculateScore(words: List<String>, keywords: List<String>): Double {
        val matches = words.count { it in keywords }
        val score = min(0.99, matches.toDouble() / 10.0)
        return (score * 100).toInt() / 100.0
    }
}

interface Vectorizer {
    fun vectorize(text: String): String
}

class HashedBagOfWordsVectorizer : Vectorizer {
    override fun vectorize(text: String): String {
        val hash = text.hashCode().toLong() and 0xFFFFFFFFL
        return "local://vectors/hash:${hash.toString(16)}"
    }
}
