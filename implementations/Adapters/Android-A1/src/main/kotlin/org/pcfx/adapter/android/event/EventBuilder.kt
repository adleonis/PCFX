package org.pcfx.adapter.android.event

import android.content.Context
import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.pcfx.adapter.android.model.ExposureEvent
import org.pcfx.adapter.android.security.KeyManager
import java.security.Signature
import java.time.Instant
import java.util.UUID
import android.util.Base64

class EventBuilder(private val context: Context) {
    private val keyManager = KeyManager(context)
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    companion object {
        private const val ADAPTER_ID = "org.pcfx.adapter.android/0.1.0"
    }

    fun buildAppFocusEvent(
        packageName: String,
        windowTitle: String?,
        consentId: String,
        retentionDays: Int
    ): ExposureEvent {
        val source = ExposureEvent.Source(
            surface = "app",
            app = packageName,
            frame = "main"
        )

        val content = ExposureEvent.Content(
            kind = "text",
            text = windowTitle ?: packageName,
            lang = "en",
            blobRef = null
        )

        return createEvent(
            source = source,
            content = content,
            consentId = consentId,
            retentionDays = retentionDays,
            capabilitiesUsed = listOf("screen.focus.read")
        )
    }

    fun buildNotificationEvent(
        packageName: String,
        notificationTitle: String?,
        notificationText: String?,
        consentId: String,
        retentionDays: Int
    ): ExposureEvent {
        val source = ExposureEvent.Source(
            surface = "system",
            app = packageName,
            frame = null
        )

        val text = listOf(notificationTitle, notificationText)
            .filterNotNull()
            .take(2)
            .joinToString(" | ")
            .take(500)

        val content = ExposureEvent.Content(
            kind = "text",
            text = text.ifEmpty { packageName },
            lang = "en",
            blobRef = null
        )

        return createEvent(
            source = source,
            content = content,
            consentId = consentId,
            retentionDays = retentionDays,
            capabilitiesUsed = listOf("notifications.read")
        )
    }

    fun buildMediaEvent(
        surface: String, // "audio", "image", "video"
        kind: String,
        blobRef: String,
        consentId: String,
        retentionDays: Int,
        capabilitiesUsed: List<String>
    ): ExposureEvent {
        val source = ExposureEvent.Source(
            surface = surface,
            frame = null
        )

        val content = ExposureEvent.Content(
            kind = kind,
            text = null,
            lang = null,
            blobRef = blobRef
        )

        return createEvent(
            source = source,
            content = content,
            consentId = consentId,
            retentionDays = retentionDays,
            capabilitiesUsed = capabilitiesUsed
        )
    }

    fun buildVideoRecordingStartEvent(
        outputFilePath: String,
        consentId: String,
        retentionDays: Int
    ): ExposureEvent {
        val source = ExposureEvent.Source(
            surface = "video",
            frame = null
        )

        val content = ExposureEvent.Content(
            kind = "video",
            text = "Screen recording started: $outputFilePath",
            lang = "en",
            blobRef = null
        )

        return createEvent(
            source = source,
            content = content,
            consentId = consentId,
            retentionDays = retentionDays,
            capabilitiesUsed = listOf("screen.video.record")
        )
    }

    fun buildVideoRecordingStopEvent(
        outputFilePath: String,
        durationSeconds: Long,
        consentId: String,
        retentionDays: Int,
        totalChunks: Int = 0
    ): ExposureEvent {
        val source = ExposureEvent.Source(
            surface = "video",
            frame = null
        )

        val text = if (totalChunks > 0) {
            "Screen recording stopped. Duration: ${durationSeconds}s, Chunks: $totalChunks"
        } else {
            "Screen recording stopped. File: $outputFilePath, Duration: ${durationSeconds}s"
        }

        val content = ExposureEvent.Content(
            kind = "video",
            text = text,
            lang = "en",
            blobRef = null
        )

        return createEvent(
            source = source,
            content = content,
            consentId = consentId,
            retentionDays = retentionDays,
            capabilitiesUsed = listOf("screen.video.record")
        )
    }

    fun buildVideoChunkEvent(
        sequenceNumber: Int,
        chunkDurationMs: Long,
        frameCount: Int,
        fileSizeBytes: Long,
        blobRef: String,
        consentId: String,
        retentionDays: Int
    ): ExposureEvent {
        val source = ExposureEvent.Source(
            surface = "video",
            frame = null
        )

        val content = ExposureEvent.Content(
            kind = "video",
            text = "Video chunk $sequenceNumber: ${chunkDurationMs}ms, $frameCount frames, $fileSizeBytes bytes",
            lang = "en",
            blobRef = blobRef
        )

        return createEvent(
            source = source,
            content = content,
            consentId = consentId,
            retentionDays = retentionDays,
            capabilitiesUsed = listOf("screen.video.record")
        )
    }

    fun buildScreenshotTextEvent(
        extractedText: String,
        textLanguage: String = "en",
        ocrConfidence: Float = 0.5f,
        ocrBlockCount: Int = 0,
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
            text = extractedText,
            lang = textLanguage,
            blobRef = null
        )

        val extensions = mutableMapOf<String, Any>(
            "ocr_confidence" to ocrConfidence,
            "ocr_block_count" to ocrBlockCount,
            "ocr_text_length" to extractedText.length
        )

        if (previousEventId != null) {
            extensions["duplicate_of_event_id"] = previousEventId
        }

        val event = ExposureEvent(
            source = source,
            content = content,
            device = getDeviceIdentifier(),
            adapterId = ADAPTER_ID,
            capabilitiesUsed = capabilities,
            privacy = ExposureEvent.Privacy(
                consentId = consentId,
                piiFlags = emptyList(),
                retentionDays = retentionDays
            ),
            signature = "",
            extensions = extensions
        )

        val signature = signEvent(event)
        return event.copy(signature = signature)
    }

    private fun createEvent(
        source: ExposureEvent.Source,
        content: ExposureEvent.Content,
        consentId: String,
        retentionDays: Int,
        capabilitiesUsed: List<String>
    ): ExposureEvent {
        val event = ExposureEvent(
            id = UUID.randomUUID().toString(),
            ts = Instant.now().toString(),
            device = getDeviceIdentifier(),
            adapterId = ADAPTER_ID,
            capabilitiesUsed = capabilitiesUsed,
            source = source,
            content = content,
            privacy = ExposureEvent.Privacy(
                consentId = consentId,
                piiFlags = emptyList(),
                retentionDays = retentionDays
            ),
            signature = "" // Will be signed
        )

        val signature = signEvent(event)
        return event.copy(signature = signature)
    }

    private fun signEvent(event: ExposureEvent): String {
        val keyPair = keyManager.getOrGenerateKeyPair()

        // Create a copy without signature for signing
        val eventForSigning = event.copy(signature = "")
        val eventJson = gson.toJson(eventForSigning)

        val signer = Signature.getInstance("SHA256withECDSA").apply {
            initSign(keyPair.privateKey)
            update(eventJson.toByteArray(Charsets.UTF_8))
        }

        val signatureBytes = signer.sign()
        val signatureBase64 = Base64.encodeToString(signatureBytes, Base64.DEFAULT)

        return "ecdsa-p256:$signatureBase64"
    }

    private fun getDeviceIdentifier(): String {
        val brand = Build.BRAND
        val model = Build.MODEL
        return "android:$brand:$model"
    }

    fun eventToJson(event: ExposureEvent): String {
        return gson.toJson(event)
    }
}
