package org.pcfx.adapter.android.screenshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ScreenshotCaptureManager(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "pcfx_screenshot_config",
        Context.MODE_PRIVATE
    )

    private var isCapturing = false
    private val lock = Any()

    companion object {
        private const val TAG = "ScreenshotCaptureManager"
        private const val PREFS_INTERVAL = "screenshot_interval_seconds"
        private const val PREFS_DEFAULT_INTERVAL = 2
        private const val PREFS_ENABLED = "screenshot_enabled"
        const val REQUEST_MEDIA_PROJECTION = 1002
    }

    fun requestScreenCapturePermission(activity: AppCompatActivity) {
        val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val intent = mediaProjectionManager.createScreenCaptureIntent()
        activity.startActivityForResult(intent, REQUEST_MEDIA_PROJECTION)
    }

    fun handleScreenCaptureResult(
        resultCode: Int,
        data: Intent?,
        intervalSeconds: Int = getInterval(),
        consentId: String,
        retentionDays: Int = 30
    ) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            Log.w(TAG, "Screen capture permission denied or cancelled")
            return
        }

        synchronized(lock) {
            if (isCapturing) {
                Log.w(TAG, "Capture already in progress")
                return
            }

            if (intervalSeconds < 1 || intervalSeconds > 5) {
                Log.w(TAG, "Invalid interval: $intervalSeconds, clamping to 1-5 range")
                val validInterval = intervalSeconds.coerceIn(1, 5)
                return handleScreenCaptureResult(resultCode, data, validInterval, consentId, retentionDays)
            }

            isCapturing = true
            saveInterval(intervalSeconds)
            setEnabled(true)

            Log.d(TAG, "Starting screenshot capture (interval: ${intervalSeconds}s, consent: $consentId)")
            ScreenshotCaptureService.start(
                context = context,
                intervalSeconds = intervalSeconds,
                consentId = consentId,
                retentionDays = retentionDays,
                resultCode = resultCode,
                projectionData = data
            )
        }
    }

    fun startCapture(
        intervalSeconds: Int = getInterval(),
        consentId: String,
        retentionDays: Int = 30
    ) {
        Log.d(TAG, "Starting screenshot capture request flow")
        // Note: This method should not be called directly. Use requestScreenCapturePermission instead.
    }

    fun stopCapture() {
        synchronized(lock) {
            if (!isCapturing) {
                Log.w(TAG, "Capture not in progress")
                return
            }

            isCapturing = false
            setEnabled(false)

            Log.d(TAG, "Stopping screenshot capture")
            ScreenshotCaptureService.stop(context)
        }
    }

    fun isCapturing(): Boolean = synchronized(lock) { isCapturing }

    fun setInterval(seconds: Int) {
        val validSeconds = seconds.coerceIn(1, 5)
        saveInterval(validSeconds)
        Log.d(TAG, "Screenshot interval set to ${validSeconds}s")

        if (isCapturing) {
            stopCapture()
            Thread.sleep(500)
            // Resume with new interval will be handled by UI
        }
    }

    fun getInterval(): Int {
        return prefs.getInt(PREFS_INTERVAL, PREFS_DEFAULT_INTERVAL)
    }

    fun isEnabled(): Boolean {
        return prefs.getBoolean(PREFS_ENABLED, false)
    }

    private fun startForegroundService(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    private fun saveInterval(seconds: Int) {
        prefs.edit().putInt(PREFS_INTERVAL, seconds).apply()
    }

    private fun setEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PREFS_ENABLED, enabled).apply()
    }
}
