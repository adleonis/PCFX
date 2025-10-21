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

    override fun onServiceConnected() {
        super.onServiceConnected()
        android.util.Log.d("AccessibilityMonitor", "Accessibility service connected and ready")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        android.util.Log.d("AccessibilityMonitor", "Event received: type=${event.eventType}, package=${event.packageName}")

        if (!isConsentGranted()) {
            android.util.Log.d("AccessibilityMonitor", "No consent granted, ignoring event")
            return
        }

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                android.util.Log.d("AccessibilityMonitor", "Window state changed event detected")
                handleWindowStateChanged(event)
            }
            else -> {
                android.util.Log.d("AccessibilityMonitor", "Event type ${event.eventType} not handled")
            }
        }
    }

    override fun onInterrupt() {
        android.util.Log.d("AccessibilityMonitor", "onInterrupt called")
    }

    private fun handleWindowStateChanged(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return

        // Avoid duplicate events for the same package
        if (packageName == lastPackage) {
            android.util.Log.d("AccessibilityMonitor", "Skipping duplicate event for package: $packageName")
            return
        }
        lastPackage = packageName

        // Try to get window title from source if available
        val source = event.source
        val windowTitle = source?.text?.toString() ?: source?.contentDescription?.toString()

        android.util.Log.d("AccessibilityMonitor", "Window changed: $packageName, title: $windowTitle")

        scope.launch {
            publishAppFocusEvent(packageName, windowTitle)
        }
    }

    private suspend fun publishAppFocusEvent(packageName: String, windowTitle: String?) {
        try {
            android.util.Log.d("AccessibilityMonitor", "publishAppFocusEvent called for package: $packageName")

            val consentManager = ConsentManager(this@AccessibilityMonitorService)
            val consent = consentManager.getActiveConsent()

            if (consent == null) {
                android.util.Log.d("AccessibilityMonitor", "No active consent, skipping event")
                return
            }

            android.util.Log.d("AccessibilityMonitor", "Building event for package: $packageName")
            val eventBuilder = EventBuilder(this@AccessibilityMonitorService)
            val exposureEvent = eventBuilder.buildAppFocusEvent(
                packageName = packageName,
                windowTitle = windowTitle,
                consentId = consent.consentId,
                retentionDays = consent.getRetentionDays("screen.focus.read")
            )

            android.util.Log.d("AccessibilityMonitor", "Event built successfully: ${exposureEvent.id}")
            val eventJson = eventBuilder.eventToJson(exposureEvent)

            // Store in local database
            val db = AppDatabase.getInstance(this@AccessibilityMonitorService)
            android.util.Log.d("AccessibilityMonitor", "Got database instance, preparing to insert event")

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

            android.util.Log.d("AccessibilityMonitor", "EventEntity created, inserting into database")
            db.eventDao().insertEvent(eventEntity)

            android.util.Log.d("AccessibilityMonitor", "Event stored in database successfully: ${exposureEvent.id}")

            // Trigger event publisher to post events
            val intent = Intent(this@AccessibilityMonitorService, EventPublisherService::class.java)
            intent.action = EventPublisherService.ACTION_PUBLISH_QUEUED_EVENTS
            startService(intent)
        } catch (e: Exception) {
            android.util.Log.e("AccessibilityMonitor", "Error publishing event: ${e.message}", e)
            e.printStackTrace()
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
