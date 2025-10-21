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
                    Log.d(TAG, "Fetched ${response.body()?.events?.size ?: 0} events since $since")
                    Result.success(response.body()!!.events)
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

    fun testConnectivity(): Result<Unit> {
        return try {
            val baseUrl = preferencesManager.getPdvBaseUrl()
            if (baseUrl.isBlank()) {
                return Result.failure(Exception("PDV base URL not configured"))
            }

            Log.d(TAG, "Testing connectivity to $baseUrl")
            Result.success(Unit)
        } catch (e: Exception) {
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
