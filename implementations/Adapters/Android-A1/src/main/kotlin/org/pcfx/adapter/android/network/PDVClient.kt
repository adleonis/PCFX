package org.pcfx.adapter.android.network

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Base64
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.pcfx.adapter.android.model.ExposureEvent
import java.io.IOException
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit

class PDVClient(private val context: Context) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("pcfx_pdv_config", Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_PDV_URL = "pdv_url"
        private const val PREFS_DEFAULT_URL = "http://127.0.0.1:7777"
    }

    fun getPDVUrl(): String {
        return sharedPrefs.getString(PREFS_PDV_URL, PREFS_DEFAULT_URL) ?: PREFS_DEFAULT_URL
    }

    fun setPDVUrl(url: String) {
        sharedPrefs.edit().putString(PREFS_PDV_URL, url).apply()
    }

    suspend fun postEvent(
        event: ExposureEvent,
        eventJson: String,
        adapterId: String,
        consentId: String,
        capabilitiesUsed: List<String>
    ): Result {
        return postEventWithRetry(
            event = event,
            eventJson = eventJson,
            adapterId = adapterId,
            consentId = consentId,
            capabilitiesUsed = capabilitiesUsed,
            retryCount = 0,
            maxRetries = 3
        )
    }

    private suspend fun postEventWithRetry(
        event: ExposureEvent,
        eventJson: String,
        adapterId: String,
        consentId: String,
        capabilitiesUsed: List<String>,
        retryCount: Int,
        maxRetries: Int
    ): Result {
        return try {
            val response = postEventRequest(
                eventJson = eventJson,
                adapterId = adapterId,
                consentId = consentId,
                capabilitiesUsed = capabilitiesUsed
            )

            when {
                response.isSuccessful -> Result.Success(event.id)
                response.code in 500..599 && retryCount < maxRetries -> {
                    val backoffMs = calculateBackoff(retryCount)
                    delay(backoffMs)
                    postEventWithRetry(
                        event = event,
                        eventJson = eventJson,
                        adapterId = adapterId,
                        consentId = consentId,
                        capabilitiesUsed = capabilitiesUsed,
                        retryCount = retryCount + 1,
                        maxRetries = maxRetries
                    )
                }
                response.code in 400..499 -> {
                    Result.Failure(
                        "Client error ${response.code}: ${response.message}",
                        isRetryable = false
                    )
                }
                else -> {
                    Result.Failure(
                        "Server error ${response.code}: ${response.message}",
                        isRetryable = true
                    )
                }
            }
        } catch (e: IOException) {
            if (retryCount < maxRetries) {
                val backoffMs = calculateBackoff(retryCount)
                delay(backoffMs)
                postEventWithRetry(
                    event = event,
                    eventJson = eventJson,
                    adapterId = adapterId,
                    consentId = consentId,
                    capabilitiesUsed = capabilitiesUsed,
                    retryCount = retryCount + 1,
                    maxRetries = maxRetries
                )
            } else {
                Result.Failure("Network error: ${e.message}", isRetryable = true)
            }
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Unknown error", isRetryable = false)
        }
    }

    private fun postEventRequest(
        eventJson: String,
        adapterId: String,
        consentId: String,
        capabilitiesUsed: List<String>
    ): okhttp3.Response {
        val url = "${getPDVUrl()}/events"
        val nonce = generateNonce()

        val headers = mapOf(
            "X-PCFX-Component" to adapterId,
            "X-PCFX-Caps" to capabilitiesUsed.joinToString(","),
            "X-PCFX-Consent" to consentId,
            "X-PCFX-Nonce" to nonce,
            "Content-Type" to "application/json",
            "Idempotency-Key" to UUID.randomUUID().toString()
        )

        val requestBody = eventJson.toRequestBody("application/json".toMediaType())
        val requestBuilder = Request.Builder()
            .url(url)
            .post(requestBody)

        headers.forEach { (key, value) ->
            requestBuilder.header(key, value)
        }

        return client.newCall(requestBuilder.build()).execute()
    }

    suspend fun postBlob(
        blobData: ByteArray,
        contentType: String,
        adapterId: String,
        consentId: String
    ): Result {
        return postBlobWithRetry(
            blobData = blobData,
            contentType = contentType,
            adapterId = adapterId,
            consentId = consentId,
            retryCount = 0,
            maxRetries = 3
        )
    }

    private suspend fun postBlobWithRetry(
        blobData: ByteArray,
        contentType: String,
        adapterId: String,
        consentId: String,
        retryCount: Int,
        maxRetries: Int
    ): Result {
        return try {
            val hash = calculateSha256(blobData)
            val url = "${getPDVUrl()}/blobs/$hash"
            val nonce = generateNonce()

            val headers = mapOf(
                "X-PCFX-Component" to adapterId,
                "X-PCFX-Caps" to "blobs.write",
                "X-PCFX-Consent" to consentId,
                "X-PCFX-Nonce" to nonce,
                "Content-Type" to contentType,
                "Idempotency-Key" to UUID.randomUUID().toString()
            )

            val requestBody = blobData.toRequestBody(contentType.toMediaType())
            val requestBuilder = Request.Builder()
                .url(url)
                .put(requestBody)

            headers.forEach { (key, value) ->
                requestBuilder.header(key, value)
            }

            val response = client.newCall(requestBuilder.build()).execute()

            when {
                response.isSuccessful -> Result.Success(hash)
                response.code in 500..599 && retryCount < maxRetries -> {
                    val backoffMs = calculateBackoff(retryCount)
                    delay(backoffMs)
                    postBlobWithRetry(
                        blobData = blobData,
                        contentType = contentType,
                        adapterId = adapterId,
                        consentId = consentId,
                        retryCount = retryCount + 1,
                        maxRetries = maxRetries
                    )
                }
                else -> Result.Failure("Blob upload failed: ${response.code}", isRetryable = true)
            }
        } catch (e: Exception) {
            if (retryCount < maxRetries) {
                val backoffMs = calculateBackoff(retryCount)
                delay(backoffMs)
                postBlobWithRetry(
                    blobData = blobData,
                    contentType = contentType,
                    adapterId = adapterId,
                    consentId = consentId,
                    retryCount = retryCount + 1,
                    maxRetries = maxRetries
                )
            } else {
                Result.Failure(e.message ?: "Unknown error", isRetryable = true)
            }
        }
    }

    suspend fun testConnectivity(): Result {
        return try {
            val healthCheckClient = PDVHealthCheckClient.getInstance(context)
            val request = Request.Builder()
                .url("${getPDVUrl()}/health")
                .header("X-App-ID", healthCheckClient.getUniqueAppId())
                .header("X-App-Type", "adapter")
                .header("X-App-Name", healthCheckClient.getAppName())
                .header("X-App-Version", healthCheckClient.getAppVersion())
                .header("X-Platform-Info", "Android ${Build.VERSION.SDK_INT}")
                .get()
                .build()

            val response = client.newCall(request).execute()
            when {
                response.isSuccessful -> Result.Success("Connected")
                response.code >= 500 -> Result.Failure("PDV server error: ${response.code}", isRetryable = true)
                else -> Result.Failure("PDV unreachable: ${response.code}", isRetryable = true)
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: e.javaClass.simpleName ?: "Unknown connection error"
            Result.Failure("Connection failed: $errorMsg", isRetryable = true)
        }
    }

    fun isOnline(): Boolean {
        val result = try {
            val request = Request.Builder()
                .url("${getPDVUrl()}/health")
                .get()
                .build()

            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }

        if (result) {
            PDVHealthCheckClient.getInstance(context).sendHealthCheck()
        }

        return result
    }

    private fun calculateBackoff(attempt: Int): Long {
        val baseMs = 500L
        val maxMs = 60_000L
        val exponential = baseMs * (1L shl attempt)
        val jitter = (Math.random() * baseMs).toLong()
        return minOf(maxMs, exponential + jitter)
    }

    private fun generateNonce(): String {
        val bytes = ByteArray(16)
        java.util.Random().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.DEFAULT).trim()
    }

    private fun calculateSha256(data: ByteArray): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data)
        return "sha256:" + hash.joinToString("") { "%02x".format(it) }
    }

    sealed class Result {
        data class Success(val data: String) : Result() // ID or hash
        data class Failure(val message: String, val isRetryable: Boolean) : Result()
    }
}
