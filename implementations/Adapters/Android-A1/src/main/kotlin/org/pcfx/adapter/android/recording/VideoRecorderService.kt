package org.pcfx.adapter.android.recording

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class VideoRecorderService : Service() {
    private var recordingThread: RecordingThread? = null
    private val stateManager = RecordingStateManager()
    private lateinit var storageManager: StorageManager
    private lateinit var config: RecordingConfig
    private lateinit var eventManager: VideoRecordingEventManager
    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private val screenStateReceiver = ScreenStateReceiver()
    private var recordingStartTime: Long = 0
    private var currentOutputFile: java.io.File? = null

    companion object {
        const val ACTION_START_RECORDING = "org.pcfx.adapter.android.START_RECORDING"
        const val ACTION_STOP_RECORDING = "org.pcfx.adapter.android.STOP_RECORDING"
        const val ACTION_PAUSE_RECORDING = "org.pcfx.adapter.android.PAUSE_RECORDING"
        const val ACTION_RESUME_RECORDING = "org.pcfx.adapter.android.RESUME_RECORDING"
        const val EXTRA_MEDIA_PROJECTION = "org.pcfx.adapter.android.MEDIA_PROJECTION"
        const val EXTRA_RESULT_CODE = "org.pcfx.adapter.android.RESULT_CODE"
        const val EXTRA_PROJECTION_DATA = "org.pcfx.adapter.android.PROJECTION_DATA"
        const val CHANNEL_ID = "pcfx_video_recording_channel"
        const val NOTIFICATION_ID = 2

        private var mediaProjectionHolder: WeakReference<MediaProjection>? = null

        fun setMediaProjection(projection: MediaProjection) {
            mediaProjectionHolder = WeakReference(projection)
        }

        fun getMediaProjection(): MediaProjection? {
            return mediaProjectionHolder?.get()
        }

        fun clearMediaProjection() {
            mediaProjectionHolder?.clear()
            mediaProjectionHolder = null
        }
    }

    override fun onCreate() {
        super.onCreate()
        config = RecordingConfig()
        storageManager = StorageManager(this, config)
        eventManager = VideoRecordingEventManager(this)
        mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        createNotificationChannel()
        registerScreenStateReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_RECORDING -> {
                showRecordingNotification()

                val resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, android.app.Activity.RESULT_CANCELED)
                val projectionData = intent.getParcelableExtra<Intent>(EXTRA_PROJECTION_DATA)

                if (resultCode == android.app.Activity.RESULT_OK && projectionData != null) {
                    try {
                        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, projectionData)
                        android.util.Log.d("VideoRecorderService", "MediaProjection obtained successfully")
                    } catch (e: Exception) {
                        android.util.Log.e("VideoRecorderService", "Error getting MediaProjection", e)
                        stateManager.setState(RecordingState.Error("Failed to get MediaProjection: ${e.message}", e))
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                        return START_NOT_STICKY
                    }
                }

                scope.launch {
                    startRecording()
                }
            }
            ACTION_STOP_RECORDING -> {
                scope.launch {
                    stopRecording()
                }
            }
            ACTION_PAUSE_RECORDING -> {
                pauseRecording()
            }
            ACTION_RESUME_RECORDING -> {
                resumeRecording()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        unregisterScreenStateReceiver()
        scope.coroutineContext[Job]?.cancel()
    }

    private suspend fun startRecording() {
        try {
            if (stateManager.isRecording()) {
                return
            }

            if (!storageManager.ensureSpace()) {
                stateManager.setState(RecordingState.Error("Insufficient storage space"))
                return
            }

            if (mediaProjection == null) {
                stateManager.setState(RecordingState.Error("MediaProjection not available."))
                stopSelf()
                return
            }

            stateManager.setState(RecordingState.Recording)
            recordingStartTime = System.currentTimeMillis()

            startChunkUploadWorker()

            val recordingDir = storageManager.getRecordingDir()
            val recordingThread = RecordingThread(recordingDir, mediaProjection!!, config, stateManager, this)
            recordingThread.start()
            this.recordingThread = recordingThread

            eventManager.emitRecordingStartEvent(recordingDir.absolutePath, recordingThread)
            android.util.Log.d("VideoRecorderService", "Recording started: ${recordingDir.absolutePath}")
        } catch (e: Exception) {
            android.util.Log.e("VideoRecorderService", "Error starting recording", e)
            stateManager.setState(RecordingState.Error("Failed to start recording: ${e.message}", e))
        }
    }

    private fun stopRecording() {
        try {
            val thread = recordingThread

            eventManager.stopMonitoring()

            thread?.stopRecording()
            recordingThread = null
            mediaProjection?.stop()
            mediaProjection = null

            if (recordingStartTime > 0) {
                val durationSeconds = (System.currentTimeMillis() - recordingStartTime) / 1000
                val totalChunks = thread?.getChunkManager()?.getAllChunks()?.size ?: 0
                eventManager.emitRecordingStopEvent(
                    recordingDirPath = storageManager.getRecordingDirPath(),
                    durationSeconds = durationSeconds,
                    totalChunks = totalChunks
                )
            }

            stopChunkUploadWorker()
            stateManager.setState(RecordingState.Idle)
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            android.util.Log.d("VideoRecorderService", "Recording stopped")
        } catch (e: Exception) {
            android.util.Log.e("VideoRecorderService", "Error stopping recording", e)
            stateManager.setState(RecordingState.Error("Failed to stop recording: ${e.message}", e))
        }
    }

    private fun pauseRecording() {
        recordingThread?.pauseRecording()
        android.util.Log.d("VideoRecorderService", "Recording paused")
    }

    private fun resumeRecording() {
        recordingThread?.resumeRecording()
        android.util.Log.d("VideoRecorderService", "Recording resumed")
    }

    private fun startChunkUploadWorker() {
        val intent = Intent(this, VideoChunkUploadWorker::class.java).apply {
            action = VideoChunkUploadWorker.ACTION_START_UPLOAD
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        android.util.Log.d("VideoRecorderService", "Chunk upload worker started")
    }

    private fun stopChunkUploadWorker() {
        val intent = Intent(this, VideoChunkUploadWorker::class.java).apply {
            action = VideoChunkUploadWorker.ACTION_STOP_UPLOAD
        }
        startService(intent)
        android.util.Log.d("VideoRecorderService", "Chunk upload worker stopped")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Video Recording",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "PCF-X Video Recording"
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun showRecordingNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Recording Video")
            .setContentText("Screen recording is active")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun registerScreenStateReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenStateReceiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(screenStateReceiver, filter)
        }
    }

    private fun unregisterScreenStateReceiver() {
        try {
            unregisterReceiver(screenStateReceiver)
        } catch (e: Exception) {
            android.util.Log.e("VideoRecorderService", "Error unregistering receiver", e)
        }
    }

    private inner class ScreenStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    pauseRecording()
                }
                Intent.ACTION_SCREEN_ON -> {
                    resumeRecording()
                }
            }
        }
    }
}
