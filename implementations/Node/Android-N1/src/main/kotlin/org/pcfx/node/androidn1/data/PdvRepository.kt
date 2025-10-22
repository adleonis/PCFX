package org.pcfx.node.androidn1.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.pcfx.node.androidn1.model.ExposureEvent
import org.pcfx.node.androidn1.model.KnowledgeAtom
import org.pcfx.node.androidn1.util.PreferencesManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PdvRepository(context: Context) {
    private val preferencesManager = PreferencesManager(context)
    private val gson = Gson()
    private var pdvService: PdvService? = null

    init {
        buildPdvService()
    }

    private fun buildPdvService() {
        val baseUrl = preferencesManager.getPdvBaseUrl()
        val logging = HttpLoggingInterceptor { message ->
            Log.d("PdvService", message)
        }
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        pdvService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PdvService::class.java)
    }

    suspend fun getEventsSince(since: String, limit: Int = 64): Result<List<ExposureEvent>> {
        return try {
            pdvService ?: run {
                buildPdvService()
                pdvService ?: return Result.failure(Exception("Failed to initialize PdvService"))
            }

            val response = pdvService!!.getEvents(since = since, limit = limit)
            when {
                response.isSuccessful && response.body() != null -> {
                    val responseBody = response.body()!!.string()
                    Log.d(TAG, "Events response body: $responseBody")

                    val responseMap = gson.fromJson(responseBody, Map::class.java) as? Map<String, Any>
                        ?: return Result.failure(Exception("Invalid response format"))

                    Log.d(TAG, "Response map keys: ${responseMap.keys}")

                    if (responseMap.containsKey("status") && responseMap["status"] == "error") {
                        val errorMsg = responseMap["message"] as? String ?: "Unknown server error"
                        Log.e(TAG, "Server error response: $errorMsg")
                        return Result.failure(Exception("Server error: $errorMsg"))
                    }

                    @Suppress("UNCHECKED_CAST")
                    val eventMaps = responseMap["events"] as? List<Map<String, Any>>
                        ?: run {
                            Log.e(TAG, "Events field: ${responseMap["events"]} (type: ${responseMap["events"]?.javaClass?.simpleName})")
                            return Result.failure(Exception("Missing or invalid events field"))
                        }

                    Log.d(TAG, "Fetched ${eventMaps.size} events since $since")

                    val events = eventMaps.mapNotNull { eventMap ->
                        try {
                            val eventData = eventMap["event"] as? Map<String, Any> ?: return@mapNotNull null
                            val eventJson = gson.toJson(eventData)
                            gson.fromJson(eventJson, ExposureEvent::class.java)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing event: ${e.message}", e)
                            null
                        }
                    }

                    Result.success(events)
                }
                response.code() == 429 -> {
                    val retryAfter = response.headers()["Retry-After"]?.toIntOrNull() ?: 30
                    Result.failure(RateLimitException(retryAfter))
                }
                response.code() >= 500 -> {
                    Result.failure(ServerException("PDV server error: ${response.code()}"))
                }
                else -> {
                    Result.failure(Exception("Failed to fetch events: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching events", e)
            Result.failure(e)
        }
    }

    suspend fun postAtom(atom: KnowledgeAtom): Result<PdvService.AtomResponse> {
        return try {
            pdvService ?: run {
                buildPdvService()
                pdvService ?: return Result.failure(Exception("Failed to initialize PdvService"))
            }

            val response = pdvService!!.postAtom(atom)
            when {
                response.isSuccessful && response.body() != null -> {
                    Log.d(TAG, "Posted atom ${atom.id}")
                    Result.success(response.body()!!)
                }
                response.code() == 429 -> {
                    val retryAfter = response.headers()["Retry-After"]?.toIntOrNull() ?: 30
                    Result.failure(RateLimitException(retryAfter))
                }
                response.code() >= 500 -> {
                    Result.failure(ServerException("PDV server error: ${response.code()}"))
                }
                response.code() == 400 -> {
                    Result.failure(ValidationException("Invalid atom schema"))
                }
                else -> {
                    Result.failure(Exception("Failed to post atom: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error posting atom", e)
            Result.failure(e)
        }
    }

    suspend fun postAtomsBatch(atoms: List<KnowledgeAtom>): Result<PdvService.BatchAtomResponse> {
        return try {
            pdvService ?: run {
                buildPdvService()
                pdvService ?: return Result.failure(Exception("Failed to initialize PdvService"))
            }

            val request = PdvService.BatchAtomRequest(atoms)
            val response = pdvService!!.postAtomsBatch(request)
            when {
                response.isSuccessful && response.body() != null -> {
                    Log.d(TAG, "Posted batch: ${response.body()!!.succeeded} succeeded, ${response.body()!!.failed} failed")
                    Result.success(response.body()!!)
                }
                response.code() == 429 -> {
                    val retryAfter = response.headers()["Retry-After"]?.toIntOrNull() ?: 30
                    Result.failure(RateLimitException(retryAfter))
                }
                response.code() >= 500 -> {
                    Result.failure(ServerException("PDV server error: ${response.code()}"))
                }
                else -> {
                    Result.failure(Exception("Failed to post atoms batch: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error posting atoms batch", e)
            Result.failure(e)
        }
    }

    suspend fun testConnectivity(): Result<Unit> {
        return try {
            pdvService ?: run {
                buildPdvService()
                pdvService ?: return Result.failure(Exception("Failed to initialize PdvService"))
            }

            val baseUrl = preferencesManager.getPdvBaseUrl()
            if (baseUrl.isBlank()) {
                return Result.failure(Exception("PDV base URL not configured"))
            }

            Log.d(TAG, "Testing connectivity to $baseUrl")
            val response = pdvService!!.checkHealth()

            when {
                response.isSuccessful -> {
                    Log.d(TAG, "PDV connectivity test successful")
                    Result.success(Unit)
                }
                response.code() >= 500 -> {
                    Result.failure(Exception("PDV server error: ${response.code()}"))
                }
                else -> {
                    Result.failure(Exception("PDV unreachable: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "PDV connectivity test failed: ${e.message ?: e.javaClass.simpleName}", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "PdvRepository"
    }
}

class RateLimitException(val retryAfterSeconds: Int) : Exception("Rate limit exceeded, retry after $retryAfterSeconds seconds")
class ServerException(message: String) : Exception(message)
class ValidationException(message: String) : Exception(message)
