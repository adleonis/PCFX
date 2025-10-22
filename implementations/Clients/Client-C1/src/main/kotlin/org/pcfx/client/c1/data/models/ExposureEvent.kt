package org.pcfx.client.c1.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ExposureEvent(
    val id: String,
    val ts: String,
    val device: String,
    val adapter_id: String,
    val schema: String = "pcfx.exposure_event/0.1",
    val capabilities_used: List<String> = emptyList(),
    val source: EventSource,
    val content: EventContent,
    val privacy: EventPrivacy,
    val signature: String = "",
    val extensions: Map<String, Any>? = null
)

@Serializable
data class EventSource(
    val surface: String,
    val app: String? = null,
    val url: String? = null,
    val frame: String? = null
)

@Serializable
data class EventContent(
    val kind: String,
    val text: String? = null,
    val lang: String? = null,
    val blob_ref: String? = null
)

@Serializable
data class EventPrivacy(
    val consent_id: String,
    val pii_flags: List<String> = emptyList(),
    val retention_days: Int = 30
)

data class EventResponse(
    val events: List<Map<String, Any>>,
    val count: Int
)
