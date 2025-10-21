package org.pcfx.adapter.android.recording

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.pcfx.adapter.android.consent.ConsentManager
import org.pcfx.adapter.android.db.AppDatabase
import org.pcfx.adapter.android.db.EventEntity
import org.pcfx.adapter.android.event.EventBuilder
import org.pcfx.adapter.android.service.EventPublisherService

class VideoRecordingEventManager(private val context: Context) {
    private val eventBuilder = EventBuilder(context)
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.Default)

    fun emitRecordingStartEvent(outputFilePath: String) {
        scope.launch {
            try {
                val consentManager = ConsentManager(context)
                val consent = consentManager.getActiveConsent()

                if (consent == null) {
                    android.util.Log.d("VideoRecordingEventManager", "No active consent, skipping recording start event")
                    return@launch
                }

                val event = eventBuilder.buildVideoRecordingStartEvent(
                    outputFilePath = outputFilePath,
                    consentId = consent.consentId,
                    retentionDays = consent.getRetentionDays("screen.video.record")
                )

                storeAndPublishEvent(event)
                android.util.Log.d("VideoRecordingEventManager", "Recording start event emitted: ${event.id}")
            } catch (e: Exception) {
                android.util.Log.e("VideoRecordingEventManager", "Error emitting recording start event", e)
            }
        }
    }

    fun emitRecordingStopEvent(outputFilePath: String, durationSeconds: Long) {
        scope.launch {
            try {
                val consentManager = ConsentManager(context)
                val consent = consentManager.getActiveConsent()

                if (consent == null) {
                    android.util.Log.d("VideoRecordingEventManager", "No active consent, skipping recording stop event")
                    return@launch
                }

                val event = eventBuilder.buildVideoRecordingStopEvent(
                    outputFilePath = outputFilePath,
                    durationSeconds = durationSeconds,
                    consentId = consent.consentId,
                    retentionDays = consent.getRetentionDays("screen.video.record")
                )

                storeAndPublishEvent(event)
                android.util.Log.d("VideoRecordingEventManager", "Recording stop event emitted: ${event.id}")
            } catch (e: Exception) {
                android.util.Log.e("VideoRecordingEventManager", "Error emitting recording stop event", e)
            }
        }
    }

    private suspend fun storeAndPublishEvent(event: org.pcfx.adapter.android.model.ExposureEvent) {
        try {
            val eventJson = eventBuilder.eventToJson(event)
            val db = AppDatabase.getInstance(context)

            val eventEntity = EventEntity(
                id = event.id,
                schema = event.schema,
                ts = event.ts,
                device = event.device,
                adapterId = event.adapterId,
                capabilitiesUsed = gson.toJson(event.capabilitiesUsed),
                sourceJson = gson.toJson(event.source),
                contentJson = gson.toJson(event.content),
                privacyJson = gson.toJson(event.privacy),
                signature = event.signature,
                eventJson = eventJson,
                isPosted = false
            )

            db.eventDao().insertEvent(eventEntity)

            val intent = Intent(context, EventPublisherService::class.java)
            intent.action = EventPublisherService.ACTION_PUBLISH_QUEUED_EVENTS
            context.startService(intent)
        } catch (e: Exception) {
            android.util.Log.e("VideoRecordingEventManager", "Error storing event", e)
        }
    }
}
