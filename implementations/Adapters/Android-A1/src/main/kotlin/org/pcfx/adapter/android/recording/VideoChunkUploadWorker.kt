package org.pcfx.adapter.android.recording

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.pcfx.adapter.android.R
import org.pcfx.adapter.android.network.PDVClient
import org.pcfx.adapter.android.consent.ConsentManager
import java.io.File

class VideoChunkUploadWorker : Service() {
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private lateinit var pdvClient: PDVClient
    private var isRunning = false

    companion object {
        private const val TAG = "VideoChunkUploadWorker"
        private const val CHANNEL_ID = "pcfx_chunk_upload_channel"
        private const val NOTIFICATION_ID = 3
        const val ACTION_START_UPLOAD = "org.pcfx.adapter.android.START_CHUNK_UPLOAD"
        const val ACTION_STOP_UPLOAD = "org.pcfx.adapter.android.STOP_CHUNK_UPLOAD"
    }

    override fun onCreate() {
        super.onCreate()
        pdvClient = PDVClient(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_UPLOAD -> {
                if (!isRunning) {
                    isRunning = true
                    showUploadNotification()
                    scope.launch {
                        runUploadLoop()
                    }
                }
                return START_STICKY
            }
            ACTION_STOP_UPLOAD -> {
                isRunning = false
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                return START_NOT_STICKY
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private suspend fun runUploadLoop() {
        try {
            val recordingDir = File(cacheDir, "recordings")
            
            while (isRunning) {
                if (!recordingDir.exists()) {
                    delay(1000)
                    continue
                }

                val chunkFiles = recordingDir.listFiles { file ->
                    file.isFile && file.extension == "mp4" && !file.name.startsWith(".")
                }?.toList() ?: emptyList()

                if (chunkFiles.isEmpty()) {
                    delay(1000)
                    continue
                }

                for (chunkFile in chunkFiles.sortedBy { it.name }) {
                    if (!isRunning) break

                    if (uploadChunkToBlob(chunkFile)) {
                        Log.d(TAG, "Successfully uploaded chunk: ${chunkFile.name}")
                        if (chunkFile.exists()) {
                            chunkFile.delete()
                        }
                    } else {
                        Log.w(TAG, "Failed to upload chunk: ${chunkFile.name}, will retry")
                        delay(5000)
                        break
                    }
                }

                delay(1000)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in upload loop", e)
        } finally {
            isRunning = false
        }
    }

    private suspend fun uploadChunkToBlob(chunkFile: File): Boolean {
        return try {
            Log.d(TAG, "Starting upload of chunk: ${chunkFile.name}, size: ${chunkFile.length()} bytes")

            val consentManager = ConsentManager(this)
            val consent = consentManager.getActiveConsent()

            if (consent == null) {
                Log.w(TAG, "No active consent, cannot upload chunk: ${chunkFile.name}")
                return false
            }

            val fileBytes = chunkFile.readBytes()
            val adapterId = "org.pcfx.adapter.android/0.1.0"
            val result = pdvClient.postBlob(
                blobData = fileBytes,
                contentType = "video/mp4",
                adapterId = adapterId,
                consentId = consent.consentId
            )

            when (result) {
                is PDVClient.Result.Success -> {
                    Log.d(TAG, "Chunk uploaded successfully: ${chunkFile.name} -> hash: ${result.data}")
                    true
                }
                is PDVClient.Result.Failure -> {
                    Log.e(TAG, "Failed to upload chunk: ${chunkFile.name}, error: ${result.message}")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading chunk: ${chunkFile.name}", e)
            false
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Video Chunk Upload",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Uploading video chunks to PDV"
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun showUploadNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Video Upload")
            .setContentText("Uploading video chunks to PDV...")
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }
}
