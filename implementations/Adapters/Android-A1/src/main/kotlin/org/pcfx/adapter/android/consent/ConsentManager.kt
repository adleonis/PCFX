package org.pcfx.adapter.android.consent

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import org.pcfx.adapter.android.model.ConsentManifest
import java.time.Instant

class ConsentManager(context: Context) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("pcfx_consent", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_CONSENT_JSON = "consent_manifest_json"
        private const val PREFS_IS_ENABLED = "consent_enabled"
    }

    fun saveConsent(manifest: ConsentManifest) {
        val json = gson.toJson(manifest)
        sharedPrefs.edit().apply {
            putString(PREFS_CONSENT_JSON, json)
            putBoolean(PREFS_IS_ENABLED, true)
            apply()
        }
    }

    fun getActiveConsent(): ConsentManifest? {
        if (!sharedPrefs.getBoolean(PREFS_IS_ENABLED, false)) {
            return null
        }

        val json = sharedPrefs.getString(PREFS_CONSENT_JSON, null) ?: return null

        return try {
            val manifest = gson.fromJson(json, ConsentManifest::class.java)
            if (manifest.isExpired()) {
                clearConsent()
                return null
            }
            manifest
        } catch (e: Exception) {
            clearConsent()
            null
        }
    }

    fun revokeConsent() {
        sharedPrefs.edit().apply {
            putBoolean(PREFS_IS_ENABLED, false)
            apply()
        }
    }

    fun clearConsent() {
        sharedPrefs.edit().apply {
            remove(PREFS_CONSENT_JSON)
            putBoolean(PREFS_IS_ENABLED, false)
            apply()
        }
    }

    fun isConsentActive(): Boolean {
        return getActiveConsent() != null
    }

    fun getAllGrants(): List<ConsentManifest.Grant> {
        return getActiveConsent()?.grants ?: emptyList()
    }

    fun hasCapability(capability: String): Boolean {
        return getAllGrants().any { it.cap == capability }
    }

    fun getGrantPurpose(capability: String): String? {
        return getAllGrants().find { it.cap == capability }?.purpose
    }

    fun formatConsentForDisplay(): String {
        val manifest = getActiveConsent() ?: return "No active consent"

        val grantsText = manifest.grants.joinToString("\n") { grant ->
            "• ${grant.cap} (${grant.purpose}, ${grant.retentionDays} days)"
        }

        return """
            |Consent ID: ${manifest.consentId}
            |Subject: ${manifest.subject}
            |Granted Capabilities:
            |$grantsText
            |
            |Created: ${manifest.createdAt}
            |Expires: ${manifest.expiresAt}
        """.trimMargin()
    }
}
