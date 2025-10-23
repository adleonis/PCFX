package org.pcfx.adapter.android.screenshot

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Display
import android.view.Surface
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel

class ScreenshotCaptureService : Service() {

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: android.media.ImageReader? = null
    private var captureThread: Thread? = null
    private var isCapturing = false
    private var captureLoopRunning = false
    private val lock = Any()

    private lateinit var screenshotQueue: ScreenshotQueue
    private lateinit var ocrProcessor: OCRProcessor
    private lateinit var deduplicator: ScreenshotDeduplicator
    private lateinit var eventBuilder: ScreenshotEventBuilder
    private lateinit var pdvClient: org.pcfx.adapter.android.network.PDVClient

    private var captureIntervalSeconds = 2
    private var consentId = ""
    private var retentionDays = 30
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())

    private var screenshotsCaptured = 0
    private var screenshotsDuplicate = 0
    private var screenshotsProcessed = 0
    private var eventsCreated = 0
    private var eventsSent = 0
    private var sessionStartTime = System.currentTimeMillis()

    companion object {
        private const val TAG = "ScreenshotCaptureService"
        const val ACTION_START_CAPTURE = "action_start_capture"
        const val ACTION_STOP_CAPTURE = "action_stop_capture"
        const val INTENT_INTERVAL = "interval_seconds"
        const val INTENT_CONSENT_ID = "consent_id"
        const val INTENT_RETENTION_DAYS = "retention_days"
        const val INTENT_RESULT_CODE = "result_code"
        const val INTENT_PROJECTION_DATA = "projection_data"
        private const val NOTIFICATION_ID = 42
        private const val NOTIFICATION_CHANNEL_ID = "screenshot_capture_channel"

        fun start(
            context: Context,
            intervalSeconds: Int,
            consentId: String,
            retentionDays: Int,
            resultCode: Int,
            projectionData: Intent
        ) {
            val intent = Intent(context, ScreenshotCaptureService::class.java).apply {
                action = ACTION_START_CAPTURE
                putExtra(INTENT_INTERVAL, intervalSeconds)
                putExtra(INTENT_CONSENT_ID, consentId)
                putExtra(INTENT_RETENTION_DAYS, retentionDays)
                putExtra(INTENT_RESULT_CODE, resultCode)
                putExtra(INTENT_PROJECTION_DATA, projectionData)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, ScreenshotCaptureService::class.java).apply {
                action = ACTION_STOP_CAPTURE
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "ScreenshotCaptureService created")
        createNotificationChannel()
        
        screenshotQueue = ScreenshotQueue(maxMemoryMB = 100)
        ocrProcessor = OCRProcessor()
        deduplicator = ScreenshotDeduplicator()
        eventBuilder = ScreenshotEventBuilder(this)
        pdvClient = org.pcfx.adapter.android.network.PDVClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        return when (action) {
            ACTION_START_CAPTURE -> {
                captureIntervalSeconds = intent.getIntExtra(INTENT_INTERVAL, 2)
                consentId = intent.getStringExtra(INTENT_CONSENT_ID) ?: ""
                retentionDays = intent.getIntExtra(INTENT_RETENTION_DAYS, 30)
                val resultCode = intent.getIntExtra(INTENT_RESULT_CODE, -1)
                val projectionData = intent.getParcelableExtra<Intent>(INTENT_PROJECTION_DATA)

                if (consentId.isEmpty()) {
                    Log.w(TAG, "Cannot start capture without consent ID")
                    stopSelf()
                    return START_NOT_STICKY
                }

                if (resultCode != android.app.Activity.RESULT_OK || projectionData == null) {
                    Log.w(TAG, "Invalid screen capture permission data")
                    stopSelf()
                    return START_NOT_STICKY
                }

                startCapture(resultCode, projectionData)
                START_STICKY
            }
            ACTION_STOP_CAPTURE -> {
                stopCapture()
                START_NOT_STICKY
            }
            else -> {
                Log.w(TAG, "Unexpected action: $action")
                START_NOT_STICKY
            }
        }
    }

    private fun startCapture(resultCode: Int, projectionData: Intent) {
        synchronized(lock) {
            if (isCapturing) {
                Log.w(TAG, "Capture already in progress")
                return
            }

            isCapturing = true
            ocrProcessor.initialize()

            val notification = buildNotification()
            try {
                startForeground(NOTIFICATION_ID, notification)
                Log.d(TAG, "Foreground service started successfully")
            } catch (e: Exception) {
                Log.w(TAG, "Failed to start foreground service: ${e.javaClass.simpleName}: ${e.message}")
                Log.w(TAG, "Continuing with background capture (will be less reliable)")
                // Continue with background capture despite the foreground service failure
                // This allows the service to keep running even if notification fails
            }

            // Initialize VirtualDisplay once
            if (!initializeVirtualDisplay(resultCode, projectionData)) {
                Log.e(TAG, "Failed to initialize VirtualDisplay")
                stopSelf()
                return
            }

            captureLoopRunning = true
            captureThread = Thread {
                runCaptureLoop()
            }.apply {
                priority = Thread.NORM_PRIORITY - 1
                start()
            }

            Log.d(TAG, "Screenshot capture started (interval: ${captureIntervalSeconds}s)")
        }
    }

    private fun stopCapture() {
        synchronized(lock) {
            if (!isCapturing) {
                Log.w(TAG, "Capture not in progress")
                return
            }

            isCapturing = false
            captureLoopRunning = false
        }

        captureThread?.join(5000)
        captureThread = null

        // Clean up media projection resources
        try {
            virtualDisplay?.release()
            virtualDisplay = null
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing VirtualDisplay", e)
        }

        try {
            mediaProjection?.stop()
            mediaProjection = null
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping MediaProjection", e)
        }

        try {
            imageReader?.close()
            imageReader = null
        } catch (e: Exception) {
            Log.e(TAG, "Error closing ImageReader", e)
        }

        screenshotQueue.clear()
        ocrProcessor.release()
        deduplicator.reset()

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()

        Log.d(TAG, "Screenshot capture stopped")
    }

    private fun runCaptureLoop() {
        try {
            Log.d(TAG, "Capture loop started")

            while (captureLoopRunning && synchronized(lock) { isCapturing }) {
                try {
                    captureScreenshot()
                    // Wait for interval - give OCR time to process previous frame
                    // This prevents overwhelming the OCR processor and ensures bitmap stays alive
                    Thread.sleep((captureIntervalSeconds * 1000).toLong())
                } catch (e: InterruptedException) {
                    Log.d(TAG, "Capture loop interrupted")
                    break
                } catch (e: Exception) {
                    Log.e(TAG, "Error in capture loop", e)
                    if (synchronized(lock) { isCapturing }) {
                        Thread.sleep(1000)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fatal error in capture thread", e)
        } finally {
            captureLoopRunning = false
            synchronized(lock) {
                isCapturing = false
            }
        }
    }

    private fun initializeVirtualDisplay(resultCode: Int, projectionData: Intent): Boolean {
        return try {
            val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val display = displayManager.getDisplay(Display.DEFAULT_DISPLAY)

            if (display == null) {
                Log.e(TAG, "Could not get default display")
                return false
            }

            val width = 1280
            val height = if (display.width > display.height) {
                (width.toFloat() * display.height / display.width).toInt()
            } else {
                (width.toFloat() * display.width / display.height).toInt()
            }

            Log.d(TAG, "Initializing VirtualDisplay - Resolution: ${width}x$height")

            imageReader = android.media.ImageReader.newInstance(
                width, height, PixelFormat.RGBA_8888, 2
            )
            val surface = imageReader!!.surface

            val projectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mediaProjection = projectionManager.getMediaProjection(resultCode, projectionData)

            if (mediaProjection == null) {
                Log.e(TAG, "Failed to get MediaProjection from result")
                imageReader?.close()
                imageReader = null
                return false
            }

            // Register MediaProjection callback
            mediaProjection?.registerCallback(object : MediaProjection.Callback() {
                override fun onStop() {
                    Log.d(TAG, "MediaProjection stopped")
                    synchronized(lock) {
                        captureLoopRunning = false
                        isCapturing = false
                    }
                }
            }, null)

            // Create VirtualDisplay (only once)
            virtualDisplay = mediaProjection?.createVirtualDisplay(
                "ScreenshotCapture",
                width, height, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                surface, null, null
            )

            Log.d(TAG, "✓ VirtualDisplay initialized successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing VirtualDisplay", e)
            false
        }
    }

    private fun captureScreenshot() {
        try {
            Log.d(TAG, "\n---------- SCREENSHOT CAPTURE START ----------")
            screenshotsCaptured++

            val image = imageReader?.acquireLatestImage() ?: run {
                Log.w(TAG, "No image available from ImageReader")
                return
            }

            // Keep image open until after bitmap is created and queued
            val bitmap = imageToBitmap(image)
            image.close()

            val timestamp = System.currentTimeMillis()
            Log.d(TAG, "✓ Bitmap captured - Size: ${bitmap.width}x${bitmap.height}, Config: ${bitmap.config}, ByteCount: ${bitmap.byteCount}")

            if (deduplicator.isDuplicate(bitmap)) {
                screenshotsDuplicate++
                val elapsedSeconds = (System.currentTimeMillis() - sessionStartTime) / 1000
                Log.d(
                    TAG,
                    "⊘ Screenshot is duplicate (total: $screenshotsCaptured captured, $screenshotsDuplicate duplicates skipped, ${screenshotsCaptured - screenshotsDuplicate} unique)"
                )
                if (elapsedSeconds > 0 && elapsedSeconds % 60 == 0L) {
                    Log.d(
                        TAG,
                        "📊 Screenshot session stats after ${elapsedSeconds}s: $screenshotsCaptured captured, $screenshotsDuplicate duplicates, $screenshotsProcessed processed, $eventsCreated events created, $eventsSent sent"
                    )
                }
                Log.d(TAG, "---------- SCREENSHOT CAPTURE END ----------\n")
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
                return
            }

            Log.d(TAG, "✓ Screenshot is unique, queuing for OCR processing")

            if (!screenshotQueue.enqueue(bitmap, timestamp)) {
                Log.w(TAG, "✗ Failed to enqueue screenshot, memory limit exceeded")
                Log.d(TAG, "---------- SCREENSHOT CAPTURE END ----------\n")
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
                return
            }

            Log.d(TAG, "✓ Screenshot enqueued successfully, queue size now processing")
            Log.d(TAG, "---------- SCREENSHOT CAPTURE END ----------\n")
            processQueuedScreenshots()
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error capturing screenshot", e)
        }
    }


    private fun imageToBitmap(image: android.media.Image): Bitmap {
        val planes = image.planes
        val buffer = planes[0].buffer
        buffer.rewind()
        val width = image.width
        val height = image.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

    private fun processQueuedScreenshots() {
        serviceScope.launch {
            while (synchronized(lock) { isCapturing }) {
                val (bitmap, timestamp) = screenshotQueue.dequeue() ?: break

                try {
                    screenshotsProcessed++
                    Log.d(TAG, "Processing dequeued screenshot (timestamp: $timestamp, processed: $screenshotsProcessed/$screenshotsCaptured)")
                    val ocrResult = ocrProcessor.processScreenshot(bitmap, timestamp)

                    if (ocrResult != null && ocrResult.text.isNotEmpty()) {
                        eventsCreated++
                        Log.d(TAG, "\n---------- EVENT BUILDING START ----------")
                        Log.d(TAG, "OCR result received:")
                        Log.d(TAG, "  - Text: \"${ocrResult.text}\"")
                        Log.d(TAG, "  - Confidence: ${ocrResult.confidence}")
                        Log.d(TAG, "  - Block count: ${ocrResult.blockCount}")
                        Log.d(TAG, "  - Language: ${ocrResult.language}")

                        val event = eventBuilder.buildScreenshotTextEvent(
                            ocrResult = ocrResult,
                            consentId = consentId,
                            retentionDays = retentionDays
                        )

                        Log.d(TAG, "📝 ExposureEvent created (total created: $eventsCreated)")
                        Log.d(TAG, "  - Event ID: ${event.signature.substring(0, minOf(16, event.signature.length))}...")
                        Log.d(TAG, "  - Capabilities used: ${event.capabilitiesUsed}")
                        Log.d(TAG, "  - Content Kind: ${event.content.kind}")
                        Log.d(TAG, "  - Content Text (from event object): ${event.content.text}")
                        Log.d(TAG, "  - Content Language: ${event.content.lang}")
                        Log.d(TAG, "---------- EVENT BUILDING END ----------\n")

                        val eventJson = com.google.gson.Gson().toJson(event)
                        Log.d(TAG, "Event JSON (full): $eventJson")

                        // Parse and verify text in JSON
                        try {
                            val jsonObj = com.google.gson.JsonParser.parseString(eventJson).asJsonObject
                            val contentObj = jsonObj.getAsJsonObject("content")
                            val textInJson = contentObj?.get("text")?.asString
                            Log.d(TAG, "✓ Text field verified in JSON: $textInJson")
                        } catch (e: Exception) {
                            Log.e(TAG, "✗ Failed to verify text in JSON: ${e.message}")
                        }

                        sendEventToPDV(event, eventJson)
                    } else {
                        Log.w(TAG, "✗ No OCR result or empty text returned")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "✗ Error processing screenshot", e)
                } finally {
                    // Recycle bitmap now that OCR is complete
                    try {
                        if (!bitmap.isRecycled) {
                            bitmap.recycle()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error recycling bitmap", e)
                    }
                }
            }
        }
    }

    private fun sendEventToPDV(event: org.pcfx.adapter.android.model.ExposureEvent, eventJson: String) {
        serviceScope.launch {
            try {
                Log.d(TAG, "\n---------- EVENT SUBMISSION START ----------")
                Log.d(TAG, "Sending ExposureEvent to PDV...")
                Log.d(TAG, "  - Adapter ID: org.pcfx.adapter.android/0.1.0")
                Log.d(TAG, "  - Consent ID: $consentId")
                Log.d(TAG, "  - Content: ${event.content.text?.substring(0, minOf(50, event.content.text?.length ?: 0))}...")

                val result = pdvClient.postEvent(
                    event = event,
                    eventJson = eventJson,
                    adapterId = "org.pcfx.adapter.android/0.1.0",
                    consentId = consentId,
                    capabilitiesUsed = event.capabilitiesUsed
                )

                when (result) {
                    is org.pcfx.adapter.android.network.PDVClient.Result.Success -> {
                        eventsSent++
                        val elapsedSeconds = (System.currentTimeMillis() - sessionStartTime) / 1000
                        Log.d(
                            TAG,
                            "✓ Screenshot event successfully sent to PDV (total sent: $eventsSent/$eventsCreated)"
                        )
                        Log.d(TAG, "  - Response: ${result.data}")
                        if (elapsedSeconds > 0 && elapsedSeconds % 60 == 0L) {
                            Log.d(
                                TAG,
                                "📊 Screenshot session stats after ${elapsedSeconds}s: $screenshotsCaptured captured, $screenshotsDuplicate duplicates, $screenshotsProcessed processed, $eventsCreated events created, $eventsSent sent"
                            )
                        }
                        Log.d(TAG, "---------- EVENT SUBMISSION END ----------\n")
                    }
                    is org.pcfx.adapter.android.network.PDVClient.Result.Failure -> {
                        Log.e(TAG, "✗ Failed to send screenshot event to PDV")
                        Log.e(TAG, "  - Error: ${result.message}")
                        Log.d(TAG, "---------- EVENT SUBMISSION END ----------\n")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "✗ Exception sending event to PDV", e)
                Log.d(TAG, "---------- EVENT SUBMISSION END ----------\n")
            }
        }
    }

    private fun buildNotification(): Notification {
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Screenshot Capture")
            .setContentText("Capturing screen and extracting text...")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Screenshot Capture",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setSound(null, null)
                enableVibration(false)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        stopCapture()
        serviceScope.cancel()
        Log.d(TAG, "ScreenshotCaptureService destroyed")
    }

    private fun String?.getUniqueAppId(): String = this ?: "unknown"
}
