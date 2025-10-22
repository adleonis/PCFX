package org.pcfx.pdv.androidpdv1.domain

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.pcfx.pdv.androidpdv1.data.dao.ConsentDao
import org.pcfx.pdv.androidpdv1.data.entity.ConsentEntity
import java.time.Instant
import java.util.UUID

class ConsentRepository(private val consentDao: ConsentDao) {
    private val gson = Gson()

    suspend fun insertConsent(
        consentJson: String,
        adapterId: String? = null,
        nodeId: String? = null
    ): Result<String> {
        return try {
            val jsonObj = gson.fromJson(consentJson, JsonObject::class.java)
            val consentId = jsonObj.get("consent_id")?.asString ?: UUID.randomUUID().toString()
            val expiresAt = jsonObj.get("expires_at")?.asString ?: Instant.now().plusSeconds(86400).toString()
            val capsJson = jsonObj.get("grants")?.toString() ?: "[]"
            val signature = jsonObj.get("signature")?.asString ?: ""

            val consent = ConsentEntity(
                consentId = consentId,
                adapterId = adapterId,
                nodeId = nodeId,
                capsJson = capsJson,
                consentJson = consentJson,
                expiresAt = expiresAt,
                signature = signature
            )
            consentDao.insertConsent(consent)
            Log.d(TAG, "Inserted consent: $consentId")
            Result.success(consentId)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting consent", e)
            Result.failure(e)
        }
    }

    suspend fun getConsentById(consentId: String): ConsentEntity? {
        return try {
            consentDao.getConsentById(consentId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting consent", e)
            null
        }
    }

    suspend fun getConsentsByAdapterId(adapterId: String): List<ConsentEntity> {
        return try {
            consentDao.getConsentsByAdapterId(adapterId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting consents for adapter", e)
            emptyList()
        }
    }

    suspend fun getConsentsByNodeId(nodeId: String): List<ConsentEntity> {
        return try {
            consentDao.getConsentsByNodeId(nodeId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting consents for node", e)
            emptyList()
        }
    }

    suspend fun getActiveConsents(): List<ConsentEntity> {
        return try {
            consentDao.getActiveConsents()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting active consents", e)
            emptyList()
        }
    }

    suspend fun verifyCapability(
        componentId: String,
        requiredCapability: String
    ): Boolean {
        return try {
            val consents = getConsentsByAdapterId(componentId) + getConsentsByNodeId(componentId)
            val now = Instant.now()

            for (consent in consents) {
                val expiresAt = Instant.parse(consent.expiresAt)
                if (now.isBefore(expiresAt)) {
                    val capsArray = gson.fromJson(consent.capsJson, com.google.gson.JsonArray::class.java)
                    for (cap in capsArray) {
                        val capObj = cap.asJsonObject
                        val capName = capObj.get("cap")?.asString ?: ""
                        if (capName == requiredCapability) {
                            return true
                        }
                    }
                }
            }
            false
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying capability", e)
            false
        }
    }

    suspend fun deleteExpiredConsents() {
        try {
            consentDao.deleteExpiredConsents()
            Log.d(TAG, "Deleted expired consents")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting expired consents", e)
        }
    }

    companion object {
        private const val TAG = "ConsentRepository"
    }
}
