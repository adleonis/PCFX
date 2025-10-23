package org.pcfx.adapter.android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.pcfx.adapter.android.R
import org.pcfx.adapter.android.db.AppDatabase
import org.pcfx.adapter.android.network.PDVClient
import org.pcfx.adapter.android.consent.ConsentManager
import com.google.gson.Gson
import org.pcfx.adapter.android.model.ExposureEvent

class EventPublisherService : Service() {
    private var serviceJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + (serviceJob ?: Job()))
    private lateinit var pdvClient: PDVClient
    private lateinit var db: AppDatabase
    private val gson = Gson()
    private var eventCountPublished = 0
    private var eventCountFailed = 0
    private var sessionStartTime = System.currentTimeMillis()

    companion object {
        const val ACTION_PUBLISH_QUEUED_EVENTS = "org.pcfx.adapter.android.PUBLISH_QUEUED"
        const val ACTION_FLUSH_NOW = "org.pcfx.adapter.android.FLUSH_NOW"
        const val CHANNEL_ID = "pcfx_adapter_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        pdvClient = PDVClient(this)
        db = AppDatabase.getInstance(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PUBLISH_QUEUED_EVENTS, ACTION_FLUSH_NOW -> {
                scope.launch {
                    publishQueuedEvents()
                    stopSelfIfNoMoreEvents()
                }
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private suspend fun publishQueuedEvents() {
        try {
            val consentManager = ConsentManager(this@EventPublisherService)
            val consent = consentManager.getActiveConsent() ?: return

            val unpostedEvents = db.eventDao().getUnpostedEvents(limit = 32)
            if (unpostedEvents.isEmpty()) {
                android.util.Log.d(
                    "EventPublisher",
                    "📊 No unposted events to publish (total published: $eventCountPublished, failed: $eventCountFailed)"
                )
                return
            }

            android.util.Log.d(
                "EventPublisher",
                "📝 Publishing ${unpostedEvents.size} queued events (total published: $eventCountPublished, failed: $eventCountFailed)"
            )

            val adapterId = "org.pcfx.adapter.android/0.1.0"

            for ((index, eventEntity) in unpostedEvents.withIndex()) {
                val result = pdvClient.postEvent(
                    event = gson.fromJson(eventEntity.eventJson, ExposureEvent::class.java),
                    eventJson = eventEntity.eventJson,
                    adapterId = adapterId,
                    consentId = consent.consentId,
                    capabilitiesUsed = gson.fromJson(
                        eventEntity.capabilitiesUsed,
                        Array<String>::class.java
                    ).toList()
                )

                when (result) {
                    is PDVClient.Result.Success -> {
                        eventCountPublished++
                        val updated = eventEntity.copy(isPosted = true)
                        db.eventDao().updateEvent(updated)
                        android.util.Log.d(
                            "EventPublisher",
                            "✓ Event ${index + 1}/${unpostedEvents.size} published (total: $eventCountPublished)"
                        )
                    }
                    is PDVClient.Result.Failure -> {
                        eventCountFailed++
                        val updated = eventEntity.copy(
                            lastAttemptAt = System.currentTimeMillis(),
                            attemptCount = eventEntity.attemptCount + 1
                        )
                        db.eventDao().updateEvent(updated)
                        android.util.Log.w(
                            "EventPublisher",
                            "✗ Event ${index + 1}/${unpostedEvents.size} failed: ${result.message} (total failed: $eventCountFailed)"
                        )

                        // Stop retrying after 5 attempts
                        if (updated.attemptCount >= 5) {
                            db.eventDao().deleteEvent(updated)
                        }
                    }
                }
            }

            // Cleanup old posted events
            val thirtyDaysAgoMs = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
            db.eventDao().deletePostedEventsOlderThan(thirtyDaysAgoMs)

            val elapsedSeconds = (System.currentTimeMillis() - sessionStartTime) / 1000
            android.util.Log.d(
                "EventPublisher",
                "📊 Batch complete: published $eventCountPublished, failed $eventCountFailed (session: ${elapsedSeconds}s)"
            )
        } catch (e: Exception) {
            android.util.Log.e("EventPublisher", "Error publishing events", e)
        }
    }

    private suspend fun stopSelfIfNoMoreEvents() {
        val unpostedCount = db.eventDao().getUnpostedCount()
        if (unpostedCount == 0) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "PCF-X Adapter",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "PCF-X Adapter Event Publishing"
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("PCF-X Adapter: ON")
            .setContentText("Publishing exposure events...")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }
}
