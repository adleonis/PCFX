package org.pcfx.adapter.android.screenshot

import android.content.Context
import android.util.Log
import org.pcfx.adapter.android.event.EventBuilder
import org.pcfx.adapter.android.model.ExposureEvent

class ScreenshotEventBuilder(private val context: Context) {

    private val eventBuilder = EventBuilder(context)

    companion object {
        private const val TAG = "ScreenshotEventBuilder"
    }

    fun buildScreenshotTextEvent(
        ocrResult: OCRResult,
        consentId: String,
        retentionDays: Int,
        previousEventId: String? = null
    ): ExposureEvent {
        Log.d(TAG, "Building ExposureEvent from OCR result...")
        Log.d(TAG, "  - OCRResult.text exists: ${ocrResult.text != null}")
        Log.d(TAG, "  - OCRResult.text is empty: ${ocrResult.text.isEmpty()}")
        Log.d(TAG, "  - OCRResult.text length: ${ocrResult.text.length}")
        Log.d(TAG, "  - Text to include: \"${ocrResult.text}\"")
        Log.d(TAG, "  - Consent ID: $consentId")
        Log.d(TAG, "  - Retention days: $retentionDays")

        val capabilities = if (previousEventId != null) {
            listOf("screen.ocr.read", "screen.ocr.deduplicate")
        } else {
            listOf("screen.ocr.read")
        }
        Log.d(TAG, "  - Capabilities: $capabilities")

        val source = ExposureEvent.Source(
            surface = "android-screenshot",
            app = null,
            url = null,
            frame = if (previousEventId != null) "duplicate" else "unique"
        )

        val content = ExposureEvent.Content(
            kind = "ocr-text",
            text = ocrResult.text,
            lang = ocrResult.language,
            blobRef = null
        )

        Log.d(TAG, "Content object created:")
        Log.d(TAG, "  - content.text: \"${content.text}\"")
        Log.d(TAG, "  - content.kind: ${content.kind}")
        Log.d(TAG, "  - content.lang: ${content.lang}")

        val device = getDeviceIdentifier()
        Log.d(TAG, "  - Device: $device")

        val event = ExposureEvent(
            source = source,
            content = content,
            device = device,
            adapterId = "org.pcfx.adapter.android/0.1.0",
            capabilitiesUsed = capabilities,
            privacy = ExposureEvent.Privacy(
                consentId = consentId,
                piiFlags = emptyList(),
                retentionDays = retentionDays
            ),
            signature = "",
            extensions = buildExtensions(ocrResult, previousEventId)
        )

        Log.d(TAG, "Event object created before signing:")
        Log.d(TAG, "  - event.content.text: \"${event.content.text}\"")
        Log.d(TAG, "  - event.content exists: ${event.content != null}")

        Log.d(TAG, "Signing event...")
        val signature = signEvent(event)
        Log.d(TAG, "  - Signature generated: ${signature.substring(0, minOf(30, signature.length))}...")

        val finalEvent = event.copy(signature = signature)
        Log.d(TAG, "Final event after signing:")
        Log.d(TAG, "  - finalEvent.content.text: \"${finalEvent.content.text}\"")
        Log.d(TAG, "  - finalEvent.content exists: ${finalEvent.content != null}")

        return finalEvent
    }

    private fun buildExtensions(
        ocrResult: OCRResult,
        previousEventId: String?
    ): Map<String, Any> {
        val ext = mutableMapOf<String, Any>(
            "ocr_confidence" to ocrResult.confidence,
            "ocr_block_count" to ocrResult.blockCount,
            "ocr_text_length" to ocrResult.text.length,
            "screenshot_timestamp" to ocrResult.timestamp
        )

        if (previousEventId != null) {
            ext["duplicate_of_event_id"] = previousEventId
        }

        return ext
    }

    private fun signEvent(event: ExposureEvent): String {
        val keyManager = org.pcfx.adapter.android.security.KeyManager(context)
        val keyPair = keyManager.getOrGenerateKeyPair()

        Log.d(TAG, "signEvent - Before copy:")
        Log.d(TAG, "  - event.content.text: \"${event.content.text}\"")

        val eventForSigning = event.copy(signature = "")
        Log.d(TAG, "signEvent - After copy (before JSON):")
        Log.d(TAG, "  - eventForSigning.content.text: \"${eventForSigning.content.text}\"")

        val eventJson = com.google.gson.Gson().toJson(eventForSigning)
        Log.d(TAG, "signEvent - JSON serialized:")
        Log.d(TAG, "  - eventJson length: ${eventJson.length}")

        try {
            val contentInJson = com.google.gson.JsonParser.parseString(eventJson).asJsonObject
                .getAsJsonObject("content")?.get("text")?.asString
            Log.d(TAG, "  - text in JSON: \"$contentInJson\"")
        } catch (e: Exception) {
            Log.d(TAG, "  - Failed to parse JSON for verification: ${e.message}")
        }

        val signer = java.security.Signature.getInstance("SHA256withECDSA").apply {
            initSign(keyPair.privateKey)
            update(eventJson.toByteArray(Charsets.UTF_8))
        }

        val signatureBytes = signer.sign()
        val signatureBase64 = android.util.Base64.encodeToString(signatureBytes, android.util.Base64.DEFAULT)

        return "ecdsa-p256:$signatureBase64"
    }

    private fun getDeviceIdentifier(): String {
        val brand = android.os.Build.BRAND
        val model = android.os.Build.MODEL
        return "android:$brand:$model"
    }
}
