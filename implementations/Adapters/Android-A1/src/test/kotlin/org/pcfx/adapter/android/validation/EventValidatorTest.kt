package org.pcfx.adapter.android.validation

import android.content.Context
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.pcfx.adapter.android.model.ExposureEvent
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EventValidatorTest {
    @Mock
    private lateinit var context: Context

    private lateinit var validator: EventValidator
    private val gson = Gson()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        validator = EventValidator(context)
    }

    @Test
    fun testValidateValidEvent() {
        val event = ExposureEvent(
            id = "test-id",
            ts = "2025-10-20T14:32:11.123Z",
            device = "android:pixel8",
            adapterId = "org.pcfx.adapter.android/0.1.0",
            capabilitiesUsed = listOf("screen.focus.read"),
            source = ExposureEvent.Source(surface = "app", app = "com.example.app"),
            content = ExposureEvent.Content(kind = "text", text = "Test"),
            privacy = ExposureEvent.Privacy(consentId = "consent-123", retentionDays = 30),
            signature = "ecdsa-p256:test"
        )

        val json = gson.toJson(event)
        val result = validator.validateEventStructure(json)
        assertTrue(result is EventValidator.ValidationResult.Valid)
    }

    @Test
    fun testValidateMissingRequiredField() {
        val json = """{
            "schema": "pcfx.exposure_event/0.1",
            "id": "test-id",
            "ts": "2025-10-20T14:32:11.123Z",
            "device": "android:pixel8"
        }"""

        val result = validator.validateEventStructure(json)
        assertTrue(result is EventValidator.ValidationResult.Invalid)
        assertTrue((result as EventValidator.ValidationResult.Invalid).message.contains("Missing required field"))
    }

    @Test
    fun testValidateInvalidSchema() {
        val json = """{
            "schema": "invalid.schema/1.0",
            "id": "test-id",
            "ts": "2025-10-20T14:32:11.123Z",
            "device": "android:pixel8",
            "adapter_id": "org.pcfx.adapter.android/0.1.0",
            "capabilities_used": ["screen.focus.read"],
            "source": {"surface": "app"},
            "content": {"kind": "text"},
            "privacy": {"consent_id": "123", "retention_days": 30},
            "signature": "test"
        }"""

        val result = validator.validateEventStructure(json)
        assertTrue(result is EventValidator.ValidationResult.Invalid)
        assertTrue((result as EventValidator.ValidationResult.Invalid).message.contains("Invalid schema version"))
    }

    @Test
    fun testValidateInvalidSurface() {
        val json = """{
            "schema": "pcfx.exposure_event/0.1",
            "id": "test-id",
            "ts": "2025-10-20T14:32:11.123Z",
            "device": "android:pixel8",
            "adapter_id": "org.pcfx.adapter.android/0.1.0",
            "capabilities_used": ["screen.focus.read"],
            "source": {"surface": "invalid_surface"},
            "content": {"kind": "text"},
            "privacy": {"consent_id": "123", "retention_days": 30},
            "signature": "test"
        }"""

        val result = validator.validateEventStructure(json)
        assertTrue(result is EventValidator.ValidationResult.Invalid)
        assertTrue((result as EventValidator.ValidationResult.Invalid).message.contains("Invalid surface"))
    }

    @Test
    fun testValidateInvalidContentKind() {
        val json = """{
            "schema": "pcfx.exposure_event/0.1",
            "id": "test-id",
            "ts": "2025-10-20T14:32:11.123Z",
            "device": "android:pixel8",
            "adapter_id": "org.pcfx.adapter.android/0.1.0",
            "capabilities_used": ["screen.focus.read"],
            "source": {"surface": "app"},
            "content": {"kind": "invalid_kind"},
            "privacy": {"consent_id": "123", "retention_days": 30},
            "signature": "test"
        }"""

        val result = validator.validateEventStructure(json)
        assertTrue(result is EventValidator.ValidationResult.Invalid)
        assertTrue((result as EventValidator.ValidationResult.Invalid).message.contains("Invalid content kind"))
    }

    @Test
    fun testValidateNegativeRetention() {
        val json = """{
            "schema": "pcfx.exposure_event/0.1",
            "id": "test-id",
            "ts": "2025-10-20T14:32:11.123Z",
            "device": "android:pixel8",
            "adapter_id": "org.pcfx.adapter.android/0.1.0",
            "capabilities_used": ["screen.focus.read"],
            "source": {"surface": "app"},
            "content": {"kind": "text"},
            "privacy": {"consent_id": "123", "retention_days": -1},
            "signature": "test"
        }"""

        val result = validator.validateEventStructure(json)
        assertTrue(result is EventValidator.ValidationResult.Invalid)
        assertTrue((result as EventValidator.ValidationResult.Invalid).message.contains("retention_days must be >= 0"))
    }

    @Test
    fun testValidateInvalidJson() {
        val invalidJson = "{ invalid json"

        val result = validator.validateEventStructure(invalidJson)
        assertTrue(result is EventValidator.ValidationResult.Invalid)
    }
}
