package org.pcfx.node.androidn1.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.pcfx.node.androidn1.model.ExposureEvent
import java.time.Instant

class RuleBasedNERTest {
    private lateinit var ner: RuleBasedNER

    @Before
    fun setUp() {
        ner = RuleBasedNER()
    }

    @Test
    fun testBrandExtraction() {
        val text = "I love Brand Apple and Brand Google"
        val entities = ner.extract(text)
        val brands = entities.filter { it.type == "org" }
        assertTrue("Should extract brands", brands.isNotEmpty())
    }

    @Test
    fun testHandleExtraction() {
        val text = "Check out @john and @jane for updates"
        val entities = ner.extract(text)
        val handles = entities.filter { it.type == "person" }
        assertEquals("Should extract 2 handles", 2, handles.size)
    }

    @Test
    fun testHashtagExtraction() {
        val text = "This is trending #viral #marketing"
        val entities = ner.extract(text)
        val hashtags = entities.filter { it.type == "topic" }
        assertEquals("Should extract 2 hashtags", 2, hashtags.size)
    }

    @Test
    fun testURLExtraction() {
        val text = "Visit https://example.com and http://test.org"
        val entities = ner.extract(text)
        val urls = entities.filter { it.type == "url" }
        assertEquals("Should extract 2 URLs", 2, urls.size)
    }

    @Test
    fun testDeduplication() {
        val text = "@user @user #tag #tag"
        val entities = ner.extract(text)
        assertEquals("Should deduplicate entities", 2, entities.size)
    }
}

class RuleBasedToneClassifierTest {
    private lateinit var classifier: RuleBasedToneClassifier

    @Before
    fun setUp() {
        classifier = RuleBasedToneClassifier()
    }

    @Test
    fun testAspirationTone() {
        val text = "This is amazing and incredible, the best achievement ever!"
        val scores = classifier.classify(text)
        assertTrue("Aspiration should be detected", scores["aspiration"]!! > 0.3)
    }

    @Test
    fun testFearTone() {
        val text = "Dangerous threat! This is scary and warning!"
        val scores = classifier.classify(text)
        assertTrue("Fear should be detected", scores["fear"]!! > 0.3)
    }

    @Test
    fun testScarcityTone() {
        val text = "Limited time offer, exclusive rare opportunity now!"
        val scores = classifier.classify(text)
        assertTrue("Scarcity should be detected", scores["scarcity"]!! > 0.3)
    }

    @Test
    fun testAllTonesReturnValue() {
        val text = "Some neutral text"
        val scores = classifier.classify(text)
        assertEquals("Should have all tone categories", 7, scores.size)
        assertTrue("All scores should be between 0 and 1", scores.values.all { it in 0.0..1.0 })
    }
}

class HashedBagOfWordsVectorizerTest {
    private lateinit var vectorizer: HashedBagOfWordsVectorizer

    @Before
    fun setUp() {
        vectorizer = HashedBagOfWordsVectorizer()
    }

    @Test
    fun testVectorization() {
        val text = "Sample text for vectorization"
        val vector = vectorizer.vectorize(text)
        assertTrue("Vector should start with prefix", vector.startsWith("local://vectors/hash:"))
    }

    @Test
    fun testDeterministicVectorization() {
        val text = "Same text should produce same vector"
        val vector1 = vectorizer.vectorize(text)
        val vector2 = vectorizer.vectorize(text)
        assertEquals("Same text should produce same vector", vector1, vector2)
    }

    @Test
    fun testDifferentTextsProduceDifferentVectors() {
        val text1 = "First text"
        val text2 = "Second text"
        val vector1 = vectorizer.vectorize(text1)
        val vector2 = vectorizer.vectorize(text2)
        assertNotEquals("Different texts should produce different vectors", vector1, vector2)
    }
}

class DefaultAtomizerTest {
    private lateinit var atomizer: DefaultAtomizer

    @Before
    fun setUp() {
        atomizer = DefaultAtomizer()
    }

    @Test
    fun testAtomizationProducesValidAtom() {
        val event = createSampleEvent(
            text = "This is an amazing @brand product with #quality"
        )
        val atoms = atomizer.process(listOf(event)).run {
            org.junit.Assert.assertEquals("Should produce 1 atom", 1, size)
            this
        }

        val atom = atoms.first()
        assertEquals("Text should match", event.content.text, atom.text)
        assertTrue("Should have provenance", atom.provenance.eventId == event.id)
        assertTrue("Should have entities", atom.entities.isNotEmpty())
        assertTrue("Should have tone", atom.tone.isNotEmpty())
        assertTrue("Should have confidence", atom.confidence.isNotEmpty())
        assertTrue("Should have vector ref", atom.vectorRef.isNotEmpty())
    }

    @Test
    fun testAtomizationSkipsEmptyEvents() {
        val event = createSampleEvent(text = "")
        val atoms = atomizer.process(listOf(event))
        assertEquals("Should skip empty text events", 0, atoms.size)
    }

    @Test
    fun testConfidenceScoring() {
        val shortText = createSampleEvent(text = "Hi")
        val longText = createSampleEvent(text = "This is a longer text with multiple words that should have higher confidence")

        val shortAtom = atomizer.process(listOf(shortText)).firstOrNull()
        val longAtom = atomizer.process(listOf(longText)).firstOrNull()

        assertTrue("Short text should have lower confidence", (shortAtom?.confidence?.get("extraction") ?: 0.0) < 0.8)
        assertTrue("Long text should have higher confidence", (longAtom?.confidence?.get("extraction") ?: 0.0) > 0.8)
    }

    private fun createSampleEvent(text: String): ExposureEvent {
        return ExposureEvent(
            schema = "pcfx.exposure_event/0.1",
            id = "test-event-${System.nanoTime()}",
            ts = Instant.now().toString(),
            device = "test-device",
            adapterId = "test-adapter/1.0",
            capabilitiesUsed = listOf("events.write"),
            source = ExposureEvent.Source(surface = "browser", url = "https://example.com"),
            content = ExposureEvent.Content(kind = "text", text = text),
            privacy = ExposureEvent.Privacy(consentId = "test-consent", retentionDays = 30),
            signature = "test-signature"
        )
    }
}
