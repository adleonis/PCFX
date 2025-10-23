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
            Log.d(TAG, "\n========== OCR PROCESSING START ==========")
            Log.d(TAG, "Bitmap input - Size: ${bitmap.width}x${bitmap.height}, Config: ${bitmap.config}, ByteCount: ${bitmap.byteCount}")

            val startTime = System.currentTimeMillis()

            val image = InputImage.fromBitmap(bitmap, 0)
            Log.d(TAG, "InputImage created - Width: ${image.width}, Height: ${image.height}, Format: ${image.format}")

            var ocrText = ""
            var blockCount = 0
            var confidence = 0.5f
            var processingComplete = false
            var processingError: Exception? = null
            var elementCount = 0

            Log.d(TAG, "Starting ML Kit TextRecognizer processing...")
            textRecognizer?.process(image)
                ?.addOnSuccessListener { visionText: Text ->
                    ocrText = visionText.text
                    blockCount = visionText.textBlocks.size

                    Log.d(TAG, "ML Kit OCR Result:")
                    Log.d(TAG, "  - Raw text length: ${ocrText.length} characters")
                    Log.d(TAG, "  - Block count: $blockCount")
                    Log.d(TAG, "  - Raw OCR text output: \"$ocrText\"")

                    if (visionText.textBlocks.isNotEmpty()) {
                        val confidences = mutableListOf<Float>()
                        for (block: Text.TextBlock in visionText.textBlocks) {
                            elementCount += block.lines.sumOf { it.elements.size }
                            for (line: Text.Line in block.lines) {
                                for (element: Text.Element in line.elements) {
                                    if (element.confidence > 0f) {
                                        confidences.add(element.confidence)
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "  - Total elements (words/symbols): $elementCount")
                        Log.d(TAG, "  - Confidence scores collected: ${confidences.size}")
                        confidence = if (confidences.isNotEmpty()) {
                            confidences.average().toFloat()
                        } else {
                            0.5f
                        }
                        Log.d(TAG, "  - Average confidence: ${"%f".format(confidence)}")
                    } else {
                        Log.d(TAG, "  - No text blocks found")
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
            // Wait up to 10 seconds for ML Kit to complete (1000 iterations × 10ms)
            // First-time model loading can take 5+ seconds
            while (!processingComplete && waitCount < 1000) {
                Thread.sleep(10)
                waitCount++
            }

            if (!processingComplete) {
                Log.w(TAG, "✗ OCR task timeout after ${System.currentTimeMillis() - startWait}ms (callback never fired)")
            } else {
                Log.d(TAG, "✓ OCR callback completed in ${System.currentTimeMillis() - startWait}ms")
            }

            if (processingError != null) {
                Log.e(TAG, "OCR processing error: ${processingError?.message}")
                Log.d(TAG, "========== OCR PROCESSING END ===========\n")
                null
            } else if (ocrText.isNotEmpty()) {
                val elapsedMs = System.currentTimeMillis() - startTime
                val detectedLanguage = detectLanguage(ocrText)
                Log.d(TAG, "✓ OCR SUCCESSFUL: ${elapsedMs}ms processing time")
                Log.d(TAG, "  - Text length: ${ocrText.length} characters")
                Log.d(TAG, "  - Blocks: $blockCount")
                Log.d(TAG, "  - Confidence: ${"%.2f".format(confidence)}")
                Log.d(TAG, "  - Language detected: $detectedLanguage")
                Log.d(TAG, "  - Full OCR Output: \"$ocrText\"")
                Log.d(TAG, "========== OCR PROCESSING END ===========\n")
                OCRResult(
                    text = ocrText,
                    timestamp = timestamp,
                    confidence = confidence,
                    blockCount = blockCount,
                    language = detectedLanguage
                )
            } else {
                Log.d(TAG, "✗ OCR found no text")
                Log.d(TAG, "========== OCR PROCESSING END ===========\n")
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
