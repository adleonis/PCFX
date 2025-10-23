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

            val contentObj = jsonObj.getAsJsonObject("content")
            val textField = contentObj?.get("text")?.asString ?: ""
            val contentKind = contentObj?.get("kind")?.asString ?: "unknown"

            Log.d(TAG, "========== EVENT INSERTION START ==========")
            Log.d(TAG, "Event ID: $id")
            Log.d(TAG, "Content Kind: $contentKind")
            Log.d(TAG, "Text Field Present: ${!textField.isEmpty()}")
            Log.d(TAG, "Text Length: ${textField.length}")
            if (textField.isNotEmpty()) {
                Log.d(TAG, "Text Preview: ${textField.substring(0, minOf(100, textField.length))}...")
            }
            Log.d(TAG, "JSON length: ${eventJson.length}")
            Log.d(TAG, "========== EVENT INSERTION END ==========")

            val event = EventEntity(
                id = id,
                ts = ts,
                device = device,
                adapterId = adapterId,
                eventJson = eventJson,
                signature = signature
            )
            eventDao.insertEvent(event)
            Log.d(TAG, "✓ Event persisted to database: $id")
            Result.success(id)
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error inserting event: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getEventsSince(since: String, limit: Int): List<Map<String, Any>> {
        return try {
            val events = eventDao.getEventsSince(since, limit)
            // Ensure ascending timestamp order (oldest first, newest last)
            events.sortedBy { it.ts }.map { event ->
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

    suspend fun getRecentEvents(limit: Int, offset: Int = 0): List<Map<String, Any>> {
        return try {
            val events = if (offset == 0) {
                eventDao.getRecentEvents(limit)
            } else {
                eventDao.getRecentEventsWithOffset(limit, offset)
            }

            Log.d(TAG, "========== GET RECENT EVENTS START ==========")
            Log.d(TAG, "Retrieved ${ events.size} events from database")

            events.map { event ->
                Log.d(TAG, "\n--- Processing Event: ${event.id} ---")
                val eventJson = gson.fromJson(event.eventJson, Map::class.java) as? Map<String, Any> ?: emptyMap()

                // Extract content for logging
                @Suppress("UNCHECKED_CAST")
                val content = eventJson["content"] as? Map<String, Any>
                val textField = content?.get("text") as? String ?: ""
                val contentKind = content?.get("kind") as? String ?: "unknown"

                Log.d(TAG, "Event ID: ${event.id}")
                Log.d(TAG, "Content Kind: $contentKind")
                Log.d(TAG, "Text Field Present: ${textField.isNotEmpty()}")
                Log.d(TAG, "Text Length: ${textField.length}")
                if (textField.isNotEmpty()) {
                    Log.d(TAG, "Text Preview: ${textField.substring(0, minOf(80, textField.length))}...")
                }

                val response = mapOf(
                    "id" to event.id,
                    "ts" to event.ts,
                    "device" to event.device,
                    "adapter_id" to event.adapterId,
                    "event" to eventJson
                )

                // Verify structure in response
                @Suppress("UNCHECKED_CAST")
                val responseEvent = response["event"] as? Map<String, Any>
                @Suppress("UNCHECKED_CAST")
                val responseContent = responseEvent?.get("content") as? Map<String, Any>
                val responseText = responseContent?.get("text") as? String ?: ""
                Log.d(TAG, "Response Text Field Present: ${responseText.isNotEmpty()}")
                Log.d(TAG, "Response Text Length: ${responseText.length}")

                response
            }.also {
                Log.d(TAG, "========== GET RECENT EVENTS END ==========")
                Log.d(TAG, "Returning ${it.size} events to client")
            }
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error getting recent events: ${e.message}", e)
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

    suspend fun deleteAccessibilityEventsBefore(before: String): Int {
        return try {
            val deletedCount = eventDao.deleteAccessibilityEventsBefore(before)
            Log.d(TAG, "Deleted $deletedCount accessibility events (content.kind=text) before $before")
            deletedCount
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting accessibility events", e)
            0
        }
    }

    companion object {
        private const val TAG = "EventRepository"
    }
}
