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

    companion object {
        private const val TAG = "ScreenshotCaptureService"
        const val ACTION_START_CAPTURE = "action_start_capture"
        const val ACTION_STOP_CAPTURE = "action_stop_capture"
        const val INTENT_INTERVAL = "interval_seconds"
        const val INTENT_CONSENT_ID = "consent_id"
        const val INTENT_RETENTION_DAYS = "retention_days"
        private const val NOTIFICATION_ID = 42
        private const val NOTIFICATION_CHANNEL_ID = "screenshot_capture_channel"

        fun start(
            context: Context,
            intervalSeconds: Int,
            consentId: String,
            retentionDays: Int
        ) {
            val intent = Intent(context, ScreenshotCaptureService::class.java).apply {
                action = ACTION_START_CAPTURE
                putExtra(INTENT_INTERVAL, intervalSeconds)
                putExtra(INTENT_CONSENT_ID, consentId)
                putExtra(INTENT_RETENTION_DAYS, retentionDays)
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

                if (consentId.isEmpty()) {
                    Log.w(TAG, "Cannot start capture without consent ID")
                    stopSelf()
                    return START_NOT_STICKY
                }

                startCapture()
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

    private fun startCapture() {
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
        }

        captureThread?.join(5000)
        captureThread = null

        screenshotQueue.clear()
        ocrProcessor.release()
        deduplicator.reset()

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()

        Log.d(TAG, "Screenshot capture stopped")
    }

    private fun runCaptureLoop() {
        try {
            val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val display = displayManager.getDisplay(Display.DEFAULT_DISPLAY)

            if (display == null) {
                Log.e(TAG, "Could not get default display")
                synchronized(lock) { isCapturing = false }
                return
            }

            val width = 1280
            val height = if (display.width > display.height) {
                (width.toFloat() * display.height / display.width).toInt()
            } else {
                (width.toFloat() * display.width / display.height).toInt()
            }

            Log.d(TAG, "Capture resolution: ${width}x$height")

            while (synchronized(lock) { isCapturing }) {
                try {
                    captureScreenshot(width, height)
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
            synchronized(lock) {
                isCapturing = false
            }
        }
    }

    private fun captureScreenshot(width: Int, height: Int) {
        try {
            val bitmap = captureScreenshotViaScreenReader(width, height) ?: return

            val timestamp = System.currentTimeMillis()

            if (deduplicator.isDuplicate(bitmap)) {
                Log.d(TAG, "Screenshot is duplicate, skipping OCR")
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
                return
            }

            if (!screenshotQueue.enqueue(bitmap, timestamp)) {
                Log.w(TAG, "Failed to enqueue screenshot, memory limit exceeded")
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
                return
            }

            processQueuedScreenshots()
        } catch (e: Exception) {
            Log.e(TAG, "Error capturing screenshot", e)
        }
    }

    private fun captureScreenshotViaScreenReader(width: Int, height: Int): Bitmap? {
        return try {
            val imageReader = android.media.ImageReader.newInstance(
                width, height, PixelFormat.RGBA_8888, 2
            )
            val surface = imageReader.surface

            val projectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mediaProjection = projectionManager.getMediaProjection(android.app.Activity.RESULT_OK, Intent())

            virtualDisplay = mediaProjection?.createVirtualDisplay(
                "ScreenshotCapture",
                width, height, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                surface, null, null
            )

            Thread.sleep(100)

            val image = imageReader.acquireLatestImage()
            imageReader.close()

            if (image != null) {
                val bitmap = imageToBitmap(image)
                image.close()
                bitmap
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in captureScreenshotViaScreenReader", e)
            null
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
                    val ocrResult = ocrProcessor.processScreenshot(bitmap, timestamp)

                    if (ocrResult != null && ocrResult.text.isNotEmpty()) {
                        val event = eventBuilder.buildScreenshotTextEvent(
                            ocrResult = ocrResult,
                            consentId = consentId,
                            retentionDays = retentionDays
                        )

                        val eventJson = com.google.gson.Gson().toJson(event)
                        sendEventToPDV(event, eventJson)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing screenshot", e)
                } finally {
                    if (!bitmap.isRecycled) {
                        bitmap.recycle()
                    }
                }
            }
        }
    }

    private fun sendEventToPDV(event: org.pcfx.adapter.android.model.ExposureEvent, eventJson: String) {
        serviceScope.launch {
            try {
                val result = pdvClient.postEvent(
                    event = event,
                    eventJson = eventJson,
                    adapterId = "org.pcfx.adapter.android/0.1.0",
                    consentId = consentId,
                    capabilitiesUsed = event.capabilitiesUsed
                )

                when (result) {
                    is org.pcfx.adapter.android.network.PDVClient.Result.Success -> {
                        Log.d(TAG, "Screenshot event sent to PDV: ${result.data}")
                    }
                    is org.pcfx.adapter.android.network.PDVClient.Result.Failure -> {
                        Log.e(TAG, "Failed to send screenshot event: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending event to PDV", e)
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
