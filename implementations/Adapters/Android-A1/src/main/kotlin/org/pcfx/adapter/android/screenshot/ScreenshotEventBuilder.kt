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
        val capabilities = if (previousEventId != null) {
            listOf("screen.ocr.read", "screen.ocr.deduplicate")
        } else {
            listOf("screen.ocr.read")
        }

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

        val event = ExposureEvent(
            source = source,
            content = content,
            device = getDeviceIdentifier(),
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

        val signature = signEvent(event)
        return event.copy(signature = signature)
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

        val eventForSigning = event.copy(signature = "")
        val eventJson = com.google.gson.Gson().toJson(eventForSigning)

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
