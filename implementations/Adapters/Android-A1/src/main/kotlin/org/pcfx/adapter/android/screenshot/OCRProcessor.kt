package org.pcfx.adapter.android.screenshot

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

data class OCRResult(
    val text: String,
    val timestamp: Long,
    val confidence: Float,
    val blockCount: Int,
    val language: String = "unknown"
)

class OCRProcessor {

    private var textRecognizer: com.google.mlkit.vision.text.TextRecognizer? = null
    private var isProcessing = false
    private val lock = Any()

    companion object {
        private const val TAG = "OCRProcessor"
    }

    fun initialize() {
        synchronized(lock) {
            if (textRecognizer == null) {
                textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                Log.d(TAG, "ML Kit TextRecognizer initialized")
            }
        }
    }

    fun isInitialized(): Boolean = synchronized(lock) { textRecognizer != null }

    fun processScreenshot(bitmap: Bitmap, timestamp: Long): OCRResult? {
        if (!isInitialized()) {
            Log.w(TAG, "TextRecognizer not initialized, skipping OCR")
            return null
        }

        synchronized(lock) {
            if (isProcessing) {
                Log.w(TAG, "OCR already in progress, skipping frame")
                return null
            }
            isProcessing = true
        }

        return try {
            val startTime = System.currentTimeMillis()

            val image = InputImage.fromBitmap(bitmap, 0)
            var ocrText = ""
            var blockCount = 0
            var confidence = 0.5f
            var processingComplete = false
            var processingError: Exception? = null

            textRecognizer?.process(image)
                ?.addOnSuccessListener { visionText: Text ->
                    ocrText = visionText.text
                    blockCount = visionText.textBlocks.size

                    if (visionText.textBlocks.isNotEmpty()) {
                        val confidences = mutableListOf<Float>()
                        for (block: Text.TextBlock in visionText.textBlocks) {
                            for (line: Text.Line in block.lines) {
                                for (element: Text.Element in line.elements) {
                                    if (element.confidence > 0f) {
                                        confidences.add(element.confidence)
                                    }
                                }
                            }
                        }
                        confidence = if (confidences.isNotEmpty()) {
                            confidences.average().toFloat()
                        } else {
                            0.5f
                        }
                    }
                    processingComplete = true
                }
                ?.addOnFailureListener { e: Exception ->
                    Log.e(TAG, "OCR processing failed", e)
                    processingError = e
                    processingComplete = true
                }

            val startWait = System.currentTimeMillis()
            var waitCount = 0
            while (!processingComplete && waitCount < 150) {
                Thread.sleep(10)
                waitCount++
            }

            if (!processingComplete) {
                Log.w(TAG, "OCR task timeout after ${System.currentTimeMillis() - startWait}ms")
            }

            if (processingError != null) {
                Log.e(TAG, "OCR processing error: ${processingError?.message}")
                null
            } else if (ocrText.isNotEmpty()) {
                val elapsedMs = System.currentTimeMillis() - startTime
                Log.d(TAG, "OCR completed in ${elapsedMs}ms: ${ocrText.length} chars, $blockCount blocks, confidence: ${"%.2f".format(confidence)}")
                OCRResult(
                    text = ocrText,
                    timestamp = timestamp,
                    confidence = confidence,
                    blockCount = blockCount,
                    language = detectLanguage(ocrText)
                )
            } else {
                Log.d(TAG, "OCR found no text")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during OCR processing", e)
            null
        } finally {
            synchronized(lock) {
                isProcessing = false
            }
        }
    }

    private fun detectLanguage(text: String): String {
        return when {
            text.isEmpty() -> "unknown"
            text.any { it.code in 0x0600..0x06FF } -> "ar"
            text.any { it.code in 0x4E00..0x9FFF || it.code in 0x3400..0x4DBF } -> "zh"
            text.any { it.code in 0x0400..0x04FF } -> "ru"
            else -> "en"
        }
    }

    fun release() {
        synchronized(lock) {
            textRecognizer?.close()
            textRecognizer = null
            Log.d(TAG, "TextRecognizer released")
        }
    }
}
