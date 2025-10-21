package org.pcfx.adapter.android.recording

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

class VideoRecordingHelper(private val context: Context) {
    private val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    fun requestScreenCapturePermission(activity: AppCompatActivity) {
        val intent = mediaProjectionManager.createScreenCaptureIntent()
        activity.startActivityForResult(intent, REQUEST_MEDIA_PROJECTION)
    }

    fun handleScreenCaptureResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val serviceIntent = Intent(context, VideoRecorderService::class.java).apply {
                action = VideoRecorderService.ACTION_START_RECORDING
                putExtra(VideoRecorderService.EXTRA_RESULT_CODE, resultCode)
                putExtra(VideoRecorderService.EXTRA_PROJECTION_DATA, data)
            }
            startForegroundService(serviceIntent)
        }
    }

    fun startRecording(mediaProjection: MediaProjection) {
        VideoRecorderService.setMediaProjection(mediaProjection)
        val intent = Intent(context, VideoRecorderService::class.java).apply {
            action = VideoRecorderService.ACTION_START_RECORDING
        }
        startForegroundService(intent)
    }

    fun stopRecording() {
        val intent = Intent(context, VideoRecorderService::class.java).apply {
            action = VideoRecorderService.ACTION_STOP_RECORDING
        }
        startForegroundService(intent)
    }

    fun pauseRecording() {
        val intent = Intent(context, VideoRecorderService::class.java).apply {
            action = VideoRecorderService.ACTION_PAUSE_RECORDING
        }
        startForegroundService(intent)
    }

    fun resumeRecording() {
        val intent = Intent(context, VideoRecorderService::class.java).apply {
            action = VideoRecorderService.ACTION_RESUME_RECORDING
        }
        startForegroundService(intent)
    }

    private fun startForegroundService(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    companion object {
        const val REQUEST_MEDIA_PROJECTION = 1001
    }
}
