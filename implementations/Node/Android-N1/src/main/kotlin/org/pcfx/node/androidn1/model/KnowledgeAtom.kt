package org.pcfx.node.androidn1.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.UUID

data class KnowledgeAtom(
    @SerializedName("schema")
    val schema: String = "pcfx.knowledge_atom/0.1",
    @SerializedName("id")
    val id: String,
    @SerializedName("ts")
    val ts: String,
    @SerializedName("provenance")
    val provenance: Provenance,
    @SerializedName("text")
    val text: String,
    @SerializedName("entities")
    val entities: List<Entity> = emptyList(),
    @SerializedName("tone")
    val tone: Map<String, Double>,
    @SerializedName("vector_ref")
    val vectorRef: String,
    @SerializedName("confidence")
    val confidence: Map<String, Double>,
    @SerializedName("blob_refs")
    val blobRefs: List<String> = emptyList(),
    @SerializedName("extensions")
    val extensions: Map<String, Any>? = null,
    @SerializedName("signature")
    val signature: String = ""
) {
    data class Provenance(
        @SerializedName("event_id")
        val eventId: String,
        @SerializedName("adapter_id")
        val adapterId: String,
        @SerializedName("analysis_node_id")
        val analysisNodeId: String = "pcfx.atomizer.android-n1/0.1.0"
    )

    data class Entity(
        @SerializedName("id")
        val id: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("name")
        val name: String? = null
    )

    companion object {
        fun builder() = KnowledgeAtomBuilder()
    }
}

class KnowledgeAtomBuilder {
    private var id: String = UUID.randomUUID().toString()
    private var ts: String = Instant.now().toString()
    private var provenance: KnowledgeAtom.Provenance? = null
    private var text: String = ""
    private var entities: List<KnowledgeAtom.Entity> = emptyList()
    private var tone: Map<String, Double> = emptyMap()
    private var vectorRef: String = ""
    private var confidence: Map<String, Double> = emptyMap()
    private var blobRefs: List<String> = emptyList()
    private var extensions: Map<String, Any>? = null
    private var signature: String = ""

    fun id(id: String) = apply { this.id = id }
    fun ts(ts: String) = apply { this.ts = ts }
    fun provenance(provenance: KnowledgeAtom.Provenance) = apply { this.provenance = provenance }
    fun text(text: String) = apply { this.text = text }
    fun entities(entities: List<KnowledgeAtom.Entity>) = apply { this.entities = entities }
    fun tone(tone: Map<String, Double>) = apply { this.tone = tone }
    fun vectorRef(vectorRef: String) = apply { this.vectorRef = vectorRef }
    fun confidence(confidence: Map<String, Double>) = apply { this.confidence = confidence }
    fun blobRefs(blobRefs: List<String>) = apply { this.blobRefs = blobRefs }
    fun extensions(extensions: Map<String, Any>?) = apply { this.extensions = extensions }
    fun signature(signature: String) = apply { this.signature = signature }

    fun build(): KnowledgeAtom {
        require(provenance != null) { "provenance is required" }
        require(text.isNotBlank()) { "text is required" }
        require(tone.isNotEmpty()) { "tone is required" }
        require(vectorRef.isNotBlank()) { "vectorRef is required" }
        require(confidence.isNotEmpty()) { "confidence is required" }

        return KnowledgeAtom(
            id = id,
            ts = ts,
            provenance = provenance!!,
            text = text,
            entities = entities,
            tone = tone,
            vectorRef = vectorRef,
            confidence = confidence,
            blobRefs = blobRefs,
            extensions = extensions,
            signature = signature
        )
    }
}
