package org.pcfx.client.c1.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.pcfx.client.c1.data.models.AtomResponse
import org.pcfx.client.c1.data.models.EventResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PDVClient @Inject constructor() {
    
    private lateinit var httpClient: HttpClient
    private var pdvHost: String = "localhost"
    private var pdvPort: Int = 7777

    fun initialize(host: String = "localhost", port: Int = 7777) {
        pdvHost = host
        pdvPort = port
        
        httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = false
                })
            }
        }
        Log.d(TAG, "PDVClient initialized with $host:$port")
    }

    fun setServer(host: String, port: Int) {
        pdvHost = host
        pdvPort = port
        Log.d(TAG, "PDV server configured: $host:$port")
    }

    private fun getBaseUrl() = "http://$pdvHost:$pdvPort"

    suspend fun getEvents(since: String = "1970-01-01T00:00:00Z", limit: Int = 5): Result<EventResponse> {
        return try {
            val url = "${getBaseUrl()}/events?since=$since&limit=$limit"
            Log.d(TAG, "Fetching events from: $url")
            
            val response: Map<String, Any> = httpClient.get(url).body()
            
            @Suppress("UNCHECKED_CAST")
            val eventsList = (response["events"] as? List<Map<String, Any>>) ?: emptyList()
            val count = (response["count"] as? Number)?.toInt() ?: 0
            
            val eventResponse = EventResponse(events = eventsList, count = count)
            Log.d(TAG, "Successfully fetched ${eventResponse.count} events")
            Result.success(eventResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching events", e)
            Result.failure(e)
        }
    }

    suspend fun getAtoms(since: String = "1970-01-01T00:00:00Z", limit: Int = 5): Result<AtomResponse> {
        return try {
            val url = "${getBaseUrl()}/atoms?since=$since&limit=$limit"
            Log.d(TAG, "Fetching atoms from: $url")
            
            val response: Map<String, Any> = httpClient.get(url).body()
            
            @Suppress("UNCHECKED_CAST")
            val atomsList = (response["atoms"] as? List<Map<String, Any>>) ?: emptyList()
            val count = (response["count"] as? Number)?.toInt() ?: 0
            
            val atomResponse = AtomResponse(atoms = atomsList, count = count)
            Log.d(TAG, "Successfully fetched ${atomResponse.count} atoms")
            Result.success(atomResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching atoms", e)
            Result.failure(e)
        }
    }

    suspend fun getStats(): Result<Map<String, Any>> {
        return try {
            val url = "${getBaseUrl()}/stats"
            Log.d(TAG, "Fetching stats from: $url")
            
            val response: Map<String, Any> = httpClient.get(url).body()
            Log.d(TAG, "Successfully fetched stats")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching stats", e)
            Result.failure(e)
        }
    }

    fun close() {
        if (::httpClient.isInitialized) {
            httpClient.close()
        }
    }

    companion object {
        private const val TAG = "PDVClient"
    }
}
