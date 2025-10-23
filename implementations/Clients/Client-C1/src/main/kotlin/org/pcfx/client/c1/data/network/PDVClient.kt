package org.pcfx.client.c1.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.pcfx.client.c1.data.models.AtomResponse
import org.pcfx.client.c1.data.models.EventResponse
import javax.inject.Inject
import javax.inject.Singleton

private fun JsonElement.toAny(): Any? = when (this) {
    is JsonObject -> mapValues { (_, element) -> element.toAny() }
    is kotlinx.serialization.json.JsonArray -> map { it.toAny() }
    is kotlinx.serialization.json.JsonPrimitive -> {
        when {
            this.isString -> this.content
            this.content == "null" -> null
            this.content.toIntOrNull() != null -> this.content.toInt()
            this.content.toLongOrNull() != null -> this.content.toLong()
            this.content.toDoubleOrNull() != null -> this.content.toDouble()
            this.content.toBooleanStrictOrNull() != null -> this.content.toBoolean()
            else -> this.content
        }
    }
}

private fun JsonObject.toMap(): Map<String, Any> {
    return mapValues { (_, element) -> element.toAny() ?: "" }
}

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
                    isLenient = true
                    allowSpecialFloatingPointValues = true
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

            val httpResponse = httpClient.get(url)
            val jsonString = httpResponse.body<String>()
            val jsonObject = Json.parseToJsonElement(jsonString).jsonObject

            @Suppress("UNCHECKED_CAST")
            val eventsList = jsonObject["events"]?.jsonArray?.map { it.jsonObject.toMap() } ?: emptyList()
            val count = jsonObject["count"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0

            val eventResponse = EventResponse(events = eventsList, count = count)
            Log.d(TAG, "Successfully fetched $count events")
            Result.success(eventResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching events", e)
            Result.failure(e)
        }
    }

    suspend fun getRecentEvents(limit: Int = 10, offset: Int = 0): Result<EventResponse> {
        return try {
            val url = "${getBaseUrl()}/events?order=recent&limit=$limit&offset=$offset"
            Log.d(TAG, "Fetching recent events from: $url (limit=$limit, offset=$offset)")

            val httpResponse = httpClient.get(url)
            val jsonString = httpResponse.body<String>()
            val jsonObject = Json.parseToJsonElement(jsonString).jsonObject

            @Suppress("UNCHECKED_CAST")
            val eventsList = jsonObject["events"]?.jsonArray?.map { it.jsonObject.toMap() } ?: emptyList()
            val count = jsonObject["count"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0

            val eventResponse = EventResponse(events = eventsList, count = count)
            Log.d(TAG, "Successfully fetched $count recent events")
            Result.success(eventResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching recent events", e)
            Result.failure(e)
        }
    }

    suspend fun getAtoms(since: String = "1970-01-01T00:00:00Z", limit: Int = 5): Result<AtomResponse> {
        return try {
            val url = "${getBaseUrl()}/atoms?since=$since&limit=$limit"
            Log.d(TAG, "Fetching atoms from: $url")

            val httpResponse = httpClient.get(url)
            val jsonString = httpResponse.body<String>()
            val jsonObject = Json.parseToJsonElement(jsonString).jsonObject

            @Suppress("UNCHECKED_CAST")
            val atomsList = jsonObject["atoms"]?.jsonArray?.map { it.jsonObject.toMap() } ?: emptyList()
            val count = jsonObject["count"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0

            val atomResponse = AtomResponse(atoms = atomsList, count = count)
            Log.d(TAG, "Successfully fetched $count atoms")
            Result.success(atomResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching atoms", e)
            Result.failure(e)
        }
    }

    suspend fun getRecentAtoms(limit: Int = 10, offset: Int = 0): Result<AtomResponse> {
        return try {
            val url = "${getBaseUrl()}/atoms?order=recent&limit=$limit&offset=$offset"
            Log.d(TAG, "Fetching recent atoms from: $url (limit=$limit, offset=$offset)")

            val httpResponse = httpClient.get(url)
            val jsonString = httpResponse.body<String>()
            val jsonObject = Json.parseToJsonElement(jsonString).jsonObject

            @Suppress("UNCHECKED_CAST")
            val atomsList = jsonObject["atoms"]?.jsonArray?.map { it.jsonObject.toMap() } ?: emptyList()
            val count = jsonObject["count"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0

            val atomResponse = AtomResponse(atoms = atomsList, count = count)
            Log.d(TAG, "Successfully fetched $count recent atoms")
            Result.success(atomResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching recent atoms", e)
            Result.failure(e)
        }
    }

    suspend fun getStats(): Result<Map<String, Any>> {
        return try {
            val url = "${getBaseUrl()}/stats"
            Log.d(TAG, "Fetching stats from: $url")

            val httpResponse = httpClient.get(url)
            val jsonString = httpResponse.body<String>()
            val jsonObject = Json.parseToJsonElement(jsonString).jsonObject
            val statsMap = jsonObject.toMap()

            Log.d(TAG, "Successfully fetched stats")
            Result.success(statsMap)
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
