package org.pcfx.adapter.android.event

import android.content.Context
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.pcfx.adapter.android.model.ExposureEvent
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EventBuilderTest {
    @Mock
    private lateinit var context: Context

    private lateinit var eventBuilder: EventBuilder
    private val gson = Gson()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        eventBuilder = EventBuilder(context)
    }

    @Test
    fun testBuildAppFocusEvent() {
        val event = eventBuilder.buildAppFocusEvent(
            packageName = "com.example.app",
            windowTitle = "Main Activity",
            consentId = "test-consent-id",
            retentionDays = 30
        )

        assertEquals("pcfx.exposure_event/0.1", event.schema)
        assertEquals("com.example.app", event.source.app)
        assertEquals("app", event.source.surface)
        assertEquals("Main Activity", event.content.text)
        assertEquals("text", event.content.kind)
        assertEquals("test-consent-id", event.privacy.consentId)
        assertEquals(30, event.privacy.retentionDays)
        assertTrue(event.capabilitiesUsed.contains("screen.focus.read"))
        assertNotNull(event.signature)
        assertTrue(event.signature.contains("ecdsa-p256:"))
    }

    @Test
    fun testBuildNotificationEvent() {
        val event = eventBuilder.buildNotificationEvent(
            packageName = "com.example.app",
            notificationTitle = "New Message",
            notificationText = "You have a new message from Alice",
            consentId = "test-consent-id",
            retentionDays = 14
        )

        assertEquals("pcfx.exposure_event/0.1", event.schema)
        assertEquals("com.example.app", event.source.app)
        assertEquals("system", event.source.surface)
        assertTrue(event.content.text!!.contains("New Message"))
        assertEquals("text", event.content.kind)
        assertEquals("test-consent-id", event.privacy.consentId)
        assertEquals(14, event.privacy.retentionDays)
        assertTrue(event.capabilitiesUsed.contains("notifications.read"))
        assertNotNull(event.signature)
    }

    @Test
    fun testBuildMediaEvent() {
        val event = eventBuilder.buildMediaEvent(
            surface = "audio",
            kind = "audio",
            blobRef = "sha256:abc123def456",
            consentId = "test-consent-id",
            retentionDays = 7,
            capabilitiesUsed = listOf("microphone.capture")
        )

        assertEquals("pcfx.exposure_event/0.1", event.schema)
        assertEquals("audio", event.source.surface)
        assertEquals("audio", event.content.kind)
        assertEquals("sha256:abc123def456", event.content.blobRef)
        assertNotNull(event.content.blobRef)
        assertEquals("test-consent-id", event.privacy.consentId)
        assertTrue(event.capabilitiesUsed.contains("microphone.capture"))
    }

    @Test
    fun testEventJsonSerialization() {
        val event = eventBuilder.buildAppFocusEvent(
            packageName = "com.example.app",
            windowTitle = "Main Activity",
            consentId = "test-consent-id",
            retentionDays = 30
        )

        val json = eventBuilder.eventToJson(event)
        assertNotNull(json)
        assertTrue(json.contains("pcfx.exposure_event/0.1"))
        assertTrue(json.contains("com.example.app"))
        assertTrue(json.contains("screen.focus.read"))

        // Test deserialization
        val deserializedEvent = gson.fromJson(json, ExposureEvent::class.java)
        assertEquals(event.schema, deserializedEvent.schema)
        assertEquals(event.source.app, deserializedEvent.source.app)
        assertEquals(event.privacy.consentId, deserializedEvent.privacy.consentId)
    }

    @Test
    fun testEventHasUniqueIds() {
        val event1 = eventBuilder.buildAppFocusEvent(
            packageName = "com.example.app",
            windowTitle = "Main Activity",
            consentId = "test-consent-id",
            retentionDays = 30
        )

        val event2 = eventBuilder.buildAppFocusEvent(
            packageName = "com.example.app",
            windowTitle = "Main Activity",
            consentId = "test-consent-id",
            retentionDays = 30
        )

        assertTrue(event1.id != event2.id)
    }

    @Test
    fun testEventHasValidTimestamp() {
        val event = eventBuilder.buildAppFocusEvent(
            packageName = "com.example.app",
            windowTitle = "Main Activity",
            consentId = "test-consent-id",
            retentionDays = 30
        )

        assertNotNull(event.ts)
        assertTrue(event.ts.isNotEmpty())
        // Timestamp should be ISO-8601 format
        assertTrue(event.ts.contains("T"))
        assertTrue(event.ts.contains("Z"))
    }

    @Test
    fun testNotificationTextTruncation() {
        val longText = "x".repeat(1000)
        val event = eventBuilder.buildNotificationEvent(
            packageName = "com.example.app",
            notificationTitle = "Title",
            notificationText = longText,
            consentId = "test-consent-id",
            retentionDays = 14
        )

        assertTrue(event.content.text!!.length <= 600) // Title + text combined should be truncated
    }
}
