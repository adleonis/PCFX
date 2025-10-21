package org.pcfx.adapter.android.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.UUID

data class ExposureEvent(
    @SerializedName("schema")
    val schema: String = "pcfx.exposure_event/0.1",

    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),

    @SerializedName("ts")
    val ts: String = Instant.now().toString(),

    @SerializedName("device")
    val device: String,

    @SerializedName("adapter_id")
    val adapterId: String,

    @SerializedName("capabilities_used")
    val capabilitiesUsed: List<String>,

    @SerializedName("source")
    val source: Source,

    @SerializedName("content")
    val content: Content,

    @SerializedName("privacy")
    val privacy: Privacy,

    @SerializedName("signature")
    val signature: String,

    @SerializedName("extensions")
    val extensions: Map<String, Any>? = null
) {
    data class Source(
        @SerializedName("surface")
        val surface: String, // "app", "system", etc.

        @SerializedName("app")
        val app: String? = null,

        @SerializedName("url")
        val url: String? = null,

        @SerializedName("frame")
        val frame: String? = null
    )

    data class Content(
        @SerializedName("kind")
        val kind: String, // "text", "audio", "image", etc.

        @SerializedName("text")
        val text: String? = null,

        @SerializedName("lang")
        val lang: String? = null,

        @SerializedName("blob_ref")
        val blobRef: String? = null
    )

    data class Privacy(
        @SerializedName("consent_id")
        val consentId: String,

        @SerializedName("pii_flags")
        val piiFlags: List<String> = emptyList(),

        @SerializedName("retention_days")
        val retentionDays: Int
    )
}
