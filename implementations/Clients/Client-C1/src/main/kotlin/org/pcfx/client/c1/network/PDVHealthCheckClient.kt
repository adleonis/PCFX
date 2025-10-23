package org.pcfx.client.c1.network

import android.content.Context
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.pcfx.client.c1.config.ClientBuildConfig
import java.util.concurrent.atomic.AtomicReference

class PDVHealthCheckClient private constructor(private val context: Context) {
    
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
            })
        }
    }

    suspend fun sendHealthCheck(pdvHost: String = "localhost", pdvPort: Int = 7777): Boolean {
        return try {
            val url = "http://$pdvHost:$pdvPort/health"
            Log.d(TAG, "Sending health check to $url")
            
            val response: HttpResponse = httpClient.get(url) {
                headers {
                    append("X-App-ID", ClientBuildConfig.UNIQUE_APP_ID)
                    append("X-App-Type", ClientBuildConfig.APP_TYPE)
                    append("X-App-Name", ClientBuildConfig.APP_NAME)
                    append("X-App-Version", ClientBuildConfig.APP_VERSION)
                    append("X-Platform-Info", ClientBuildConfig.PLATFORM_INFO)
                }
            }
            
            val isHealthy = response.status == HttpStatusCode.OK
            if (isHealthy) {
                Log.i(TAG, "Health check successful: PDV server is healthy")
            } else {
                Log.w(TAG, "Health check failed with status: ${response.status}")
            }
            isHealthy
        } catch (e: Exception) {
            Log.e(TAG, "Health check error: ${e.message}", e)
            false
        }
    }

    fun close() {
        httpClient.close()
    }

    companion object {
        private const val TAG = "PDVHealthCheckClient"
        private val instance = AtomicReference<PDVHealthCheckClient?>(null)

        fun getInstance(context: Context): PDVHealthCheckClient {
            return instance.getAndSet(null) ?: synchronized(this) {
                instance.get() ?: PDVHealthCheckClient(context).also {
                    instance.set(it)
                }
            }
        }
    }
}
