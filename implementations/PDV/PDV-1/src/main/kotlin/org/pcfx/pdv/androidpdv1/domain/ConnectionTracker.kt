package org.pcfx.pdv.androidpdv1.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ConnectionStats(
    val totalAdapters: Int = 0,
    val activeAdapters24h: Int = 0,
    val totalNodes: Int = 0,
    val activeNodes24h: Int = 0,
    val totalClients: Int = 0,
    val activeClients24h: Int = 0
)

class ConnectionTracker(private val healthCheckRepository: HealthCheckRepository) {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun recordConnection(
        appId: String,
        appType: String,
        appName: String = "",
        appVersion: String = "",
        platformInfo: String = ""
    ) {
        scope.launch {
            try {
                healthCheckRepository.recordOrUpdateHealthCheck(
                    appId = appId,
                    appType = appType,
                    appName = appName,
                    appVersion = appVersion,
                    platformInfo = platformInfo
                )
                Log.d(TAG, "Recorded connection: $appType - $appId")
            } catch (e: Exception) {
                Log.e(TAG, "Error recording connection", e)
            }
        }
    }

    suspend fun getStats(): ConnectionStats {
        return try {
            val now = System.currentTimeMillis()
            val day = 24 * 60 * 60 * 1000

            val totalAdapters = healthCheckRepository.getHealthCheckCountByType("adapter")
            val activeAdapters24h = healthCheckRepository.getActiveHealthChecksByTypeSince("adapter", now - day)

            val totalNodes = healthCheckRepository.getHealthCheckCountByType("node")
            val activeNodes24h = healthCheckRepository.getActiveHealthChecksByTypeSince("node", now - day)

            val totalClients = healthCheckRepository.getHealthCheckCountByType("client")
            val activeClients24h = healthCheckRepository.getActiveHealthChecksByTypeSince("client", now - day)

            ConnectionStats(
                totalAdapters = totalAdapters,
                activeAdapters24h = activeAdapters24h,
                totalNodes = totalNodes,
                activeNodes24h = activeNodes24h,
                totalClients = totalClients,
                activeClients24h = activeClients24h
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting stats", e)
            ConnectionStats()
        }
    }

    companion object {
        private const val TAG = "ConnectionTracker"
        private var instance: ConnectionTracker? = null

        fun getInstance(): ConnectionTracker {
            return instance ?: throw IllegalStateException("ConnectionTracker not initialized. Call initialize() first.")
        }

        fun initialize(healthCheckRepository: HealthCheckRepository) {
            if (instance == null) {
                instance = ConnectionTracker(healthCheckRepository)
            }
        }
    }
}
