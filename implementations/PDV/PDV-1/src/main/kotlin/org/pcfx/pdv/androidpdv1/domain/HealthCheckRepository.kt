package org.pcfx.pdv.androidpdv1.domain

import android.util.Log
import org.pcfx.pdv.androidpdv1.data.dao.HealthCheckDao
import org.pcfx.pdv.androidpdv1.data.entity.HealthCheckEntity

class HealthCheckRepository(private val healthCheckDao: HealthCheckDao) {
    suspend fun recordOrUpdateHealthCheck(
        appId: String,
        appType: String,
        appName: String,
        appVersion: String,
        platformInfo: String
    ) {
        try {
            val existing = healthCheckDao.getHealthCheckByAppId(appId)
            val now = System.currentTimeMillis()

            if (existing != null) {
                val updated = existing.copy(
                    lastConnected = now,
                    connectionCount = existing.connectionCount + 1
                )
                healthCheckDao.updateHealthCheck(updated)
                Log.d(TAG, "Updated health check for app: $appId, count: ${updated.connectionCount}")
            } else {
                val newCheck = HealthCheckEntity(
                    appId = appId,
                    appType = appType,
                    appName = appName,
                    appVersion = appVersion,
                    platformInfo = platformInfo,
                    firstConnection = now,
                    lastConnected = now,
                    connectionCount = 1
                )
                healthCheckDao.insertHealthCheck(newCheck)
                Log.d(TAG, "Inserted new health check for app: $appId")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error recording health check", e)
            throw e
        }
    }

    suspend fun getHealthCheck(appId: String): HealthCheckEntity? {
        return try {
            healthCheckDao.getHealthCheckByAppId(appId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting health check", e)
            null
        }
    }

    suspend fun getAllHealthChecks(): List<HealthCheckEntity> {
        return try {
            healthCheckDao.getAllHealthChecks()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all health checks", e)
            emptyList()
        }
    }

    suspend fun getHealthChecksByType(appType: String): List<HealthCheckEntity> {
        return try {
            healthCheckDao.getHealthChecksByType(appType)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting health checks by type", e)
            emptyList()
        }
    }

    suspend fun getTotalHealthCheckCount(): Int {
        return try {
            healthCheckDao.getHealthCheckCount()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting total health check count", e)
            0
        }
    }

    suspend fun getHealthCheckCountByType(appType: String): Int {
        return try {
            healthCheckDao.getHealthCheckCountByType(appType)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting health check count by type", e)
            0
        }
    }

    suspend fun getActiveHealthChecksSince(timestamp: Long): Int {
        return try {
            healthCheckDao.getActiveHealthChecksSince(timestamp)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting active health checks since", e)
            0
        }
    }

    suspend fun getActiveHealthChecksByTypeSince(appType: String, timestamp: Long): Int {
        return try {
            healthCheckDao.getActiveHealthChecksByTypeSince(appType, timestamp)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting active health checks by type since", e)
            0
        }
    }

    companion object {
        private const val TAG = "HealthCheckRepository"
    }
}
