package org.pcfx.client.c1.data.models

import kotlinx.serialization.Serializable

@Serializable
data class KnowledgeAtom(
    val id: String,
    val ts: String,
    val schema: String = "pcfx.knowledge_atom/0.1",
    val provenance: AtomProvenance,
    val text: String,
    val entities: List<Entity> = emptyList(),
    val tone: ToneInfo? = null,
    val vector_ref: String? = null,
    val confidence: ConfidenceInfo? = null,
    val blob_refs: List<String> = emptyList(),
    val signature: String = "",
    val extensions: Map<String, Any>? = null
)

@Serializable
data class AtomProvenance(
    val event_id: String,
    val adapter_id: String,
    val analysis_node_id: String
)

@Serializable
data class Entity(
    val id: String,
    val type: String,
    val name: String
)

@Serializable
data class ToneInfo(
    val sentiment: String? = null,
    val intensity: Double? = null,
    val emotions: List<String> = emptyList()
)

@Serializable
data class ConfidenceInfo(
    val extraction_confidence: Double? = null,
    val relevance_score: Double? = null
)

data class AtomResponse(
    val atoms: List<Map<String, Any>>,
    val count: Int
)
