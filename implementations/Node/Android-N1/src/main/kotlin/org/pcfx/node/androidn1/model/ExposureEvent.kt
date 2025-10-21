package org.pcfx.node.androidn1.model

import com.google.gson.annotations.SerializedName

data class ExposureEvent(
    @SerializedName("schema")
    val schema: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("ts")
    val ts: String,
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
    @SerializedName("extensions")
    val extensions: Map<String, Any>? = null,
    @SerializedName("signature")
    val signature: String
) {
    data class Source(
        @SerializedName("surface")
        val surface: String,
        @SerializedName("app")
        val app: String? = null,
        @SerializedName("url")
        val url: String? = null,
        @SerializedName("frame")
        val frame: String? = null
    )

    data class Content(
        @SerializedName("kind")
        val kind: String,
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
        val piiFlags: List<String>? = null,
        @SerializedName("retention_days")
        val retentionDays: Int
    )
}
