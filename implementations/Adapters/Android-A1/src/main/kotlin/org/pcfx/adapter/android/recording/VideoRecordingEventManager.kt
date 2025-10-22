package org.pcfx.adapter.android.recording

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.pcfx.adapter.android.consent.ConsentManager
import org.pcfx.adapter.android.db.AppDatabase
import org.pcfx.adapter.android.db.EventEntity
import org.pcfx.adapter.android.event.EventBuilder
import org.pcfx.adapter.android.service.EventPublisherService
import java.util.concurrent.atomic.AtomicBoolean

class VideoRecordingEventManager(private val context: Context) {
    private val eventBuilder = EventBuilder(context)
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.Default)

    private var chunkMonitorJob: Job? = null
    private val isMonitoring = AtomicBoolean(false)

    fun emitRecordingStartEvent(outputFilePath: String, recordingThread: RecordingThread) {
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
                startChunkMonitor(recordingThread)
                android.util.Log.d("VideoRecordingEventManager", "Recording start event emitted: ${event.id}")
            } catch (e: Exception) {
                android.util.Log.e("VideoRecordingEventManager", "Error emitting recording start event", e)
            }
        }
    }

    fun emitRecordingStopEvent(recordingDirPath: String, durationSeconds: Long, totalChunks: Int = 0) {
        scope.launch {
            try {
                val consentManager = ConsentManager(context)
                val consent = consentManager.getActiveConsent()

                if (consent == null) {
                    android.util.Log.d("VideoRecordingEventManager", "No active consent, skipping recording stop event")
                    return@launch
                }

                val event = eventBuilder.buildVideoRecordingStopEvent(
                    outputFilePath = recordingDirPath,
                    durationSeconds = durationSeconds,
                    totalChunks = totalChunks,
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

    private fun startChunkMonitor(recordingThread: RecordingThread) {
        if (isMonitoring.getAndSet(true)) {
            android.util.Log.w("VideoRecordingEventManager", "Chunk monitor already running")
            return
        }

        chunkMonitorJob = scope.launch {
            try {
                val chunkManager = recordingThread.getChunkManager()
                val consentManager = ConsentManager(context)
                val consent = consentManager.getActiveConsent() ?: return@launch

                while (isMonitoring.get()) {
                    val pendingChunks = chunkManager.getPendingChunks()
                    for (chunk in pendingChunks) {
                        if (!chunk.isUploaded && isMonitoring.get()) {
                            emitChunkEvent(chunk, consent)
                        }
                    }

                    if (isMonitoring.get()) {
                        kotlinx.coroutines.delay(500)
                    }
                }
                android.util.Log.d("VideoRecordingEventManager", "Chunk monitor stopped")
            } catch (e: Exception) {
                if (isMonitoring.get()) {
                    android.util.Log.e("VideoRecordingEventManager", "Error in chunk monitor", e)
                }
            } finally {
                isMonitoring.set(false)
            }
        }
    }

    fun stopMonitoring() {
        if (!isMonitoring.getAndSet(false)) {
            return
        }

        chunkMonitorJob?.cancel()
        chunkMonitorJob = null
        android.util.Log.d("VideoRecordingEventManager", "Stop monitoring requested")
    }

    private suspend fun emitChunkEvent(chunk: VideoChunk, consent: org.pcfx.adapter.android.model.ConsentManifest) {
        try {
            val blobRef = "pcfx://blob/${chunk.file.name}"
            val event = eventBuilder.buildVideoChunkEvent(
                sequenceNumber = chunk.sequenceNumber,
                chunkDurationMs = chunk.durationMs,
                frameCount = chunk.frameCount,
                fileSizeBytes = chunk.file.length(),
                blobRef = blobRef,
                consentId = consent.consentId,
                retentionDays = consent.getRetentionDays("screen.video.record")
            )

            storeAndPublishEvent(event)
            android.util.Log.d("VideoRecordingEventManager", "Chunk event emitted for sequence ${chunk.sequenceNumber}")
        } catch (e: Exception) {
            android.util.Log.e("VideoRecordingEventManager", "Error emitting chunk event", e)
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
