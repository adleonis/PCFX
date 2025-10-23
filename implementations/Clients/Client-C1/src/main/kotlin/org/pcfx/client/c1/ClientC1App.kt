package org.pcfx.client.c1

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.pcfx.client.c1.config.ClientBuildConfig
import org.pcfx.client.c1.network.PDVHealthCheckClient

@HiltAndroidApp
class ClientC1App : Application() {
    
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Client-C1 app launched")
        Log.i(TAG, "App ID: ${ClientBuildConfig.UNIQUE_APP_ID}")
        Log.i(TAG, "App Name: ${ClientBuildConfig.APP_NAME}")
        Log.i(TAG, "App Type: ${ClientBuildConfig.APP_TYPE}")
        Log.i(TAG, "App Version: ${ClientBuildConfig.APP_VERSION}")
        
        sendHealthCheck()
    }

    private fun sendHealthCheck() {
        GlobalScope.launch {
            try {
                val healthCheckClient = PDVHealthCheckClient.getInstance(this@ClientC1App)
                val isHealthy = healthCheckClient.sendHealthCheck(
                    pdvHost = ClientBuildConfig.DEFAULT_PDV_HOST,
                    pdvPort = ClientBuildConfig.DEFAULT_PDV_PORT
                )
                
                if (isHealthy) {
                    Log.i(TAG, "PDV server is healthy and reachable")
                } else {
                    Log.w(TAG, "PDV server health check failed - app may not have connectivity")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending health check: ${e.message}", e)
            }
        }
    }

    companion object {
        private const val TAG = "ClientC1App"
    }
}
