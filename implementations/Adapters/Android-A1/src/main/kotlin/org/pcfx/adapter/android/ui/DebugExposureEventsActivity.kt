package org.pcfx.adapter.android.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.pcfx.adapter.android.R
import org.pcfx.adapter.android.db.AppDatabase
import org.pcfx.adapter.android.model.ExposureEvent
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DebugExposureEventsActivity : AppCompatActivity() {
    private val gson = Gson()
    private lateinit var eventsContainer: LinearLayout
    private lateinit var loadingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_exposure_events)

        eventsContainer = findViewById(R.id.events_container)
        loadingText = findViewById(R.id.loading_text)
        val backButton = findViewById<Button>(R.id.back_button)
        val createTestEventButton = findViewById<Button>(R.id.create_test_event_button)

        backButton.setOnClickListener {
            finish()
        }

        createTestEventButton.setOnClickListener {
            createManualTestEvent()
        }

        loadEvents()
    }

    override fun onResume() {
        super.onResume()
        loadEvents()
    }

    private fun loadEvents() {
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getInstance(this@DebugExposureEventsActivity)
                val eventEntities = db.eventDao().getRecentEvents(limit = 5)

                Log.d("DebugActivity", "Found ${eventEntities.size} events in database")

                if (eventEntities.isEmpty()) {
                    loadingText.text = "No events found. Enable Accessibility Service above and switch between apps to generate events."
                    eventsContainer.visibility = android.view.View.GONE
                } else {
                    loadingText.visibility = android.view.View.GONE
                    eventsContainer.removeAllViews()

                    eventEntities.forEachIndexed { index, eventEntity ->
                        try {
                            val event = gson.fromJson(eventEntity.eventJson, ExposureEvent::class.java)
                            val eventView = createEventView(index + 1, event, eventEntity.isPosted)
                            eventsContainer.addView(eventView)
                        } catch (e: Exception) {
                            Log.e("DebugActivity", "Error parsing event: ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("DebugActivity", "Error loading events", e)
                loadingText.text = "Error loading events: ${e.message}"
            }
        }
    }

    private fun createEventView(index: Int, event: ExposureEvent, isPosted: Boolean): android.view.View {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
            setBackgroundColor(if (isPosted) 0xFFE8F5E9.toInt() else 0xFFFFF9C4.toInt())
            setPadding(16, 16, 16, 16)
        }

        val headerText = TextView(this).apply {
            text = "Event #$index ${if (isPosted) "(Posted)" else "(Pending)"}"
            textSize = 16f
            setTextColor(android.graphics.Color.BLACK)
            setTypeface(null, android.graphics.Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 8
            }
        }
        container.addView(headerText)

        val detailsText = TextView(this).apply {
            text = buildEventDetails(event)
            textSize = 12f
            setTextColor(android.graphics.Color.DKGRAY)
            setTextIsSelectable(true)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        container.addView(detailsText)

        return container
    }

    private fun buildEventDetails(event: ExposureEvent): String {
        return buildString {
            append("ID: ${event.id}\n")
            append("Timestamp: ${formatTimestamp(event.ts)}\n")
            append("Surface: ${event.source.surface}\n")
            append("App: ${event.source.app ?: "N/A"}\n")
            append("Content Type: ${event.content.kind}\n")
            append("Capabilities Used: ${event.capabilitiesUsed.joinToString(", ")}\n")
            append("Consent ID: ${event.privacy.consentId}\n")
            append("Retention Days: ${event.privacy.retentionDays}\n")
            if (!event.content.text.isNullOrEmpty()) {
                val truncatedText = if (event.content.text.length > 100) {
                    event.content.text.substring(0, 100) + "..."
                } else {
                    event.content.text
                }
                append("Content Preview: $truncatedText")
            }
        }
    }

    private fun formatTimestamp(isoTimestamp: String): String {
        return try {
            val instant = Instant.parse(isoTimestamp)
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault())
            formatter.format(instant)
        } catch (e: Exception) {
            isoTimestamp
        }
    }

    private fun createManualTestEvent() {
        lifecycleScope.launch {
            try {
                val consentManager = org.pcfx.adapter.android.consent.ConsentManager(this@DebugExposureEventsActivity)
                val consent = consentManager.getActiveConsent()

                if (consent == null) {
                    android.widget.Toast.makeText(
                        this@DebugExposureEventsActivity,
                        "No active consent. Please grant consent first.",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val eventBuilder = org.pcfx.adapter.android.event.EventBuilder(this@DebugExposureEventsActivity)
                val testEvent = eventBuilder.buildAppFocusEvent(
                    packageName = "com.android.chrome",
                    windowTitle = "Test Event from Debug Page",
                    consentId = consent.consentId,
                    retentionDays = 30
                )

                val eventJson = eventBuilder.eventToJson(testEvent)
                val db = AppDatabase.getInstance(this@DebugExposureEventsActivity)

                val eventEntity = org.pcfx.adapter.android.db.EventEntity(
                    id = testEvent.id,
                    schema = testEvent.schema,
                    ts = testEvent.ts,
                    device = testEvent.device,
                    adapterId = testEvent.adapterId,
                    capabilitiesUsed = gson.toJson(testEvent.capabilitiesUsed),
                    sourceJson = gson.toJson(testEvent.source),
                    contentJson = gson.toJson(testEvent.content),
                    privacyJson = gson.toJson(testEvent.privacy),
                    signature = testEvent.signature,
                    eventJson = eventJson,
                    isPosted = false
                )

                db.eventDao().insertEvent(eventEntity)

                Log.d("DebugActivity", "Test event created: ${testEvent.id}")
                android.widget.Toast.makeText(
                    this@DebugExposureEventsActivity,
                    "Test event created successfully!",
                    android.widget.Toast.LENGTH_SHORT
                ).show()

                loadEvents()
            } catch (e: Exception) {
                Log.e("DebugActivity", "Error creating test event", e)
                android.widget.Toast.makeText(
                    this@DebugExposureEventsActivity,
                    "Error: ${e.message}",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
