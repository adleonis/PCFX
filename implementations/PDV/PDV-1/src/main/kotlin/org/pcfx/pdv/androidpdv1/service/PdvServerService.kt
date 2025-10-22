package org.pcfx.pdv.androidpdv1.service

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
import org.pcfx.pdv.androidpdv1.R
import org.pcfx.pdv.androidpdv1.data.PdvPreferences
import org.pcfx.pdv.androidpdv1.server.PdvServer

class PdvServerService : Service() {
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var pdvServer: PdvServer? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d(TAG, "PDV Server Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_SERVER -> {
                scope.launch {
                    startServer()
                }
                return START_STICKY
            }
            ACTION_STOP_SERVER -> {
                scope.launch {
                    stopServer()
                }
                return START_NOT_STICKY
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.launch {
            stopServer()
        }
        Log.d(TAG, "PDV Server Service destroyed")
    }

    private suspend fun startServer() {
        try {
            val prefs = PdvPreferences.getInstance(this)
            val port = prefs.getPort()
            pdvServer = PdvServer(this, port)
            pdvServer?.start()

            delay(500)

            showNotification(port)
            Log.d(TAG, "PDV Server started successfully on port $port")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting PDV server", e)
            stopSelf()
        }
    }

    private suspend fun stopServer() {
        try {
            pdvServer?.stop()
            pdvServer = null
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            Log.d(TAG, "PDV Server stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping PDV server", e)
        }
    }


    private fun showNotification(port: Int) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("PDV Server running on port $port")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "PDV Server Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications for PDV Server operations"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "PdvServerService"
        private const val CHANNEL_ID = "pdv_server_channel"
        private const val NOTIFICATION_ID = 1

        const val ACTION_START_SERVER = "org.pcfx.pdv.androidpdv1.START_SERVER"
        const val ACTION_STOP_SERVER = "org.pcfx.pdv.androidpdv1.STOP_SERVER"
    }
}
