package org.pcfx.adapter.android.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.pcfx.adapter.android.consent.ConsentManager
import org.pcfx.adapter.android.event.EventBuilder
import org.pcfx.adapter.android.db.AppDatabase
import org.pcfx.adapter.android.db.EventEntity
import com.google.gson.Gson

class NotificationListenerService : NotificationListenerService() {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val gson = Gson()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        android.util.Log.d("NotificationListener", "Notification posted from ${sbn.packageName}")

        if (!isConsentGranted()) {
            android.util.Log.d("NotificationListener", "No consent granted, skipping notification")
            return
        }

        scope.launch {
            publishNotificationEvent(sbn)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // We only capture posted notifications, not removed ones
    }

    private suspend fun publishNotificationEvent(sbn: StatusBarNotification) {
        try {
            val notification = sbn.notification ?: return
            val packageName = sbn.packageName

            val extras = notification.extras
            val notificationTitle = extras.getString(android.app.Notification.EXTRA_TITLE)
            val notificationText = extras.getCharSequence(android.app.Notification.EXTRA_TEXT)?.toString()

            // Skip if minimal content
            if (notificationTitle.isNullOrBlank() && notificationText.isNullOrBlank()) {
                android.util.Log.d("NotificationListener", "Skipping notification with no title or text")
                return
            }

            val consentManager = ConsentManager(this@NotificationListenerService)
            val consent = consentManager.getActiveConsent()

            if (consent == null) {
                android.util.Log.d("NotificationListener", "No active consent, skipping notification")
                return
            }

            val eventBuilder = EventBuilder(this@NotificationListenerService)
            val exposureEvent = eventBuilder.buildNotificationEvent(
                packageName = packageName,
                notificationTitle = notificationTitle,
                notificationText = notificationText,
                consentId = consent.consentId,
                retentionDays = consent.getRetentionDays("notifications.read")
            )

            val eventJson = eventBuilder.eventToJson(exposureEvent)

            // Store in local database
            val db = AppDatabase.getInstance(this@NotificationListenerService)
            val eventEntity = EventEntity(
                id = exposureEvent.id,
                schema = exposureEvent.schema,
                ts = exposureEvent.ts,
                device = exposureEvent.device,
                adapterId = exposureEvent.adapterId,
                capabilitiesUsed = gson.toJson(exposureEvent.capabilitiesUsed),
                sourceJson = gson.toJson(exposureEvent.source),
                contentJson = gson.toJson(exposureEvent.content),
                privacyJson = gson.toJson(exposureEvent.privacy),
                signature = exposureEvent.signature,
                eventJson = eventJson,
                isPosted = false
            )

            db.eventDao().insertEvent(eventEntity)

            android.util.Log.d("NotificationListener", "Notification event stored: ${exposureEvent.id}")

            // Trigger event publisher to post events
            val intent = android.content.Intent(
                this@NotificationListenerService,
                EventPublisherService::class.java
            )
            intent.action = EventPublisherService.ACTION_PUBLISH_QUEUED_EVENTS
            startService(intent)
        } catch (e: Exception) {
            android.util.Log.e("NotificationListener", "Error publishing notification event", e)
        }
    }

    private fun isConsentGranted(): Boolean {
        return try {
            val consentManager = ConsentManager(this)
            consentManager.getActiveConsent() != null
        } catch (e: Exception) {
            false
        }
    }
}
