package org.pcfx.adapter.android.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

data class ConsentManifest(
    @SerializedName("schema")
    val schema: String = "pcfx.consent/0.1",

    @SerializedName("consent_id")
    val consentId: String = UUID.randomUUID().toString(),

    @SerializedName("subject")
    val subject: String,

    @SerializedName("adapter_id")
    val adapterId: String,

    @SerializedName("grants")
    val grants: List<Grant>,

    @SerializedName("created_at")
    val createdAt: String = Instant.now().toString(),

    @SerializedName("expires_at")
    val expiresAt: String,

    @SerializedName("signature")
    val signature: String,

    @SerializedName("extensions")
    val extensions: Map<String, Any>? = null
) {
    data class Grant(
        @SerializedName("cap")
        val cap: String,

        @SerializedName("purpose")
        val purpose: String,

        @SerializedName("retention_days")
        val retentionDays: Int
    )

    fun isExpired(): Boolean {
        val expiresInstant = Instant.parse(expiresAt)
        return Instant.now().isAfter(expiresInstant)
    }

    fun getRetentionDays(capability: String): Int {
        return grants.find { it.cap == capability }?.retentionDays ?: 30
    }
}

object ConsentManifestBuilder {
    fun default(
        adapterId: String,
        subject: String = "user:self",
        expiryDays: Long = 365
    ): ConsentManifest {
        val now = Instant.now()
        val expiresAt = now.plus(expiryDays, ChronoUnit.DAYS)

        return ConsentManifest(
            subject = subject,
            adapterId = adapterId,
            grants = listOf(
                ConsentManifest.Grant(
                    cap = "screen.focus.read",
                    purpose = "exposure_audit",
                    retentionDays = 30
                ),
                ConsentManifest.Grant(
                    cap = "notifications.read",
                    purpose = "exposure_audit",
                    retentionDays = 30
                )
            ),
            expiresAt = expiresAt.toString(),
            signature = "" // Will be signed by EventPublisher
        )
    }
}
