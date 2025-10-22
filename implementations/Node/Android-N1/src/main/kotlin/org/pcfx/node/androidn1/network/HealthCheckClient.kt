package org.pcfx.node.androidn1.network

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.pcfx.node.androidn1.config.NodeBuildConfig
import java.util.concurrent.TimeUnit

class HealthCheckClient(
    private val context: Context,
    private val pdvUrl: String = "http://127.0.0.1:7777"
) {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private val scope = CoroutineScope(Dispatchers.IO)

    fun sendHealthCheck() {
        scope.launch {
            try {
                val platformInfo = "Android ${Build.VERSION.SDK_INT}"
                val request = Request.Builder()
                    .url("$pdvUrl/health")
                    .header("X-App-ID", NodeBuildConfig.UNIQUE_APP_ID)
                    .header("X-App-Type", NodeBuildConfig.APP_TYPE)
                    .header("X-App-Name", NodeBuildConfig.APP_NAME)
                    .header("X-App-Version", NodeBuildConfig.APP_VERSION)
                    .header("X-Platform-Info", platformInfo)
                    .get()
                    .build()

                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        Log.d(TAG, "Health check sent successfully")
                    } else {
                        Log.w(TAG, "Health check failed with status: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending health check", e)
            }
        }
    }

    fun getUniqueAppId(): String = NodeBuildConfig.UNIQUE_APP_ID
    fun getAppName(): String = NodeBuildConfig.APP_NAME
    fun getAppVersion(): String = NodeBuildConfig.APP_VERSION

    companion object {
        private const val TAG = "HealthCheckClient"
        private var instance: HealthCheckClient? = null

        fun getInstance(context: Context): HealthCheckClient {
            return instance ?: HealthCheckClient(context).also { instance = it }
        }
    }
}
