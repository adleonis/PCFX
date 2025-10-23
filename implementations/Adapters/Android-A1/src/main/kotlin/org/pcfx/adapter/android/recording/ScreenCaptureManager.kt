package org.pcfx.adapter.android.recording

import android.content.Context
import android.hardware.display.DisplayManager
import android.media.projection.MediaProjection
import android.os.Build
import android.view.Surface

class ScreenCaptureManager(
    private val context: Context,
    private val mediaProjection: MediaProjection,
    private val config: RecordingConfig
) {
    private val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    private var virtualDisplay: android.hardware.display.VirtualDisplay? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            mediaProjection.registerCallback(object : MediaProjection.Callback() {
                override fun onStop() {
                    android.util.Log.d("ScreenCaptureManager", "MediaProjection stopped")
                    releaseVirtualDisplay()
                }
            }, null)
        }
    }

    fun createVirtualDisplay(surface: Surface): android.hardware.display.VirtualDisplay? {
        return try {
            virtualDisplay = mediaProjection.createVirtualDisplay(
                "ScreenCapture",
                config.width,
                config.height,
                context.resources.displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                surface,
                null,
                null
            )
            android.util.Log.d("ScreenCaptureManager", "Virtual display created successfully")
            virtualDisplay
        } catch (e: Exception) {
            android.util.Log.e("ScreenCaptureManager", "Error creating virtual display", e)
            null
        }
    }

    fun releaseVirtualDisplay() {
        try {
            virtualDisplay?.release()
            android.util.Log.d("ScreenCaptureManager", "Virtual display released")
        } catch (e: Exception) {
            android.util.Log.e("ScreenCaptureManager", "Error releasing virtual display", e)
        }
        virtualDisplay = null
    }

    fun getDisplayDimensions(): Pair<Int, Int> {
        return Pair(config.width, config.height)
    }
}
