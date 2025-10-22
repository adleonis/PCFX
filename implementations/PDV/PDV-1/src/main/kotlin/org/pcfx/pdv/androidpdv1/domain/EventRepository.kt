package org.pcfx.pdv.androidpdv1.domain

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.pcfx.pdv.androidpdv1.data.dao.EventDao
import org.pcfx.pdv.androidpdv1.data.entity.EventEntity
import java.util.UUID

@Suppress("UNCHECKED_CAST")

class EventRepository(private val eventDao: EventDao) {
    private val gson = Gson()

    suspend fun insertEvent(eventJson: String): Result<String> {
        return try {
            val jsonObj = gson.fromJson(eventJson, JsonObject::class.java)
            val id = jsonObj.get("id")?.asString ?: UUID.randomUUID().toString()
            val ts = jsonObj.get("ts")?.asString ?: ""
            val device = jsonObj.get("device")?.asString ?: "unknown"
            val adapterId = jsonObj.get("adapter_id")?.asString ?: "unknown"
            val signature = jsonObj.get("signature")?.asString ?: ""

            val event = EventEntity(
                id = id,
                ts = ts,
                device = device,
                adapterId = adapterId,
                eventJson = eventJson,
                signature = signature
            )
            eventDao.insertEvent(event)
            Log.d(TAG, "Inserted event: $id")
            Result.success(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting event", e)
            Result.failure(e)
        }
    }

    suspend fun getEventsSince(since: String, limit: Int): List<Map<String, Any>> {
        return try {
            val events = eventDao.getEventsSince(since, limit)
            events.map { event ->
                val eventJson = gson.fromJson(event.eventJson, Map::class.java) as? Map<String, Any> ?: emptyMap()
                mapOf(
                    "id" to event.id,
                    "ts" to event.ts,
                    "device" to event.device,
                    "adapter_id" to event.adapterId,
                    "event" to eventJson
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting events", e)
            emptyList()
        }
    }

    suspend fun getEventById(id: String): EventEntity? {
        return try {
            eventDao.getEventById(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting event", e)
            null
        }
    }

    suspend fun getEventCount(): Int {
        return try {
            eventDao.getEventCount()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting event count", e)
            0
        }
    }

    suspend fun deleteEventsBefore(before: String) {
        try {
            eventDao.deleteEventsBefore(before)
            Log.d(TAG, "Deleted events before $before")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting events", e)
        }
    }

    companion object {
        private const val TAG = "EventRepository"
    }
}
