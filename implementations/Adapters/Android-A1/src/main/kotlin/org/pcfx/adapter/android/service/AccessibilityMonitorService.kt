package org.pcfx.adapter.android.service

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.pcfx.adapter.android.consent.ConsentManager
import org.pcfx.adapter.android.event.EventBuilder
import org.pcfx.adapter.android.db.AppDatabase
import org.pcfx.adapter.android.db.EventEntity
import com.google.gson.Gson

class AccessibilityMonitorService : AccessibilityService() {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val gson = Gson()
    private var lastPackage: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!isConsentGranted()) {
            return
        }

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                handleWindowStateChanged(event)
            }
        }
    }

    override fun onInterrupt() {
        // Required by accessibility service interface
    }

    private fun handleWindowStateChanged(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return
        val source = event.source ?: return

        // Avoid duplicate events for the same package
        if (packageName == lastPackage) {
            return
        }
        lastPackage = packageName

        val windowTitle = source.text?.toString() ?: source.contentDescription?.toString()

        scope.launch {
            publishAppFocusEvent(packageName, windowTitle)
        }
    }

    private suspend fun publishAppFocusEvent(packageName: String, windowTitle: String?) {
        try {
            val consentManager = ConsentManager(this@AccessibilityMonitorService)
            val consent = consentManager.getActiveConsent() ?: return

            val eventBuilder = EventBuilder(this@AccessibilityMonitorService)
            val exposureEvent = eventBuilder.buildAppFocusEvent(
                packageName = packageName,
                windowTitle = windowTitle,
                consentId = consent.consentId,
                retentionDays = consent.getRetentionDays("screen.focus.read")
            )

            val eventJson = eventBuilder.eventToJson(exposureEvent)

            // Store in local database
            val db = AppDatabase.getInstance(this@AccessibilityMonitorService)
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

            // Trigger event publisher to post events
            val intent = Intent(this@AccessibilityMonitorService, EventPublisherService::class.java)
            intent.action = EventPublisherService.ACTION_PUBLISH_QUEUED_EVENTS
            startService(intent)
        } catch (e: Exception) {
            android.util.Log.e("AccessibilityMonitor", "Error publishing event", e)
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
