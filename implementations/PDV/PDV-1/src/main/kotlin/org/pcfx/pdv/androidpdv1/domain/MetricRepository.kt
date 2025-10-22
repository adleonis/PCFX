package org.pcfx.pdv.androidpdv1.domain

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.pcfx.pdv.androidpdv1.data.dao.MetricDao
import org.pcfx.pdv.androidpdv1.data.entity.MetricEntity
import java.util.UUID

@Suppress("UNCHECKED_CAST")

class MetricRepository(private val metricDao: MetricDao) {
    private val gson = Gson()

    suspend fun insertMetric(metricJson: String): Result<String> {
        return try {
            val jsonObj = gson.fromJson(metricJson, JsonObject::class.java)
            val id = jsonObj.get("id")?.asString ?: UUID.randomUUID().toString()
            val ts = jsonObj.get("ts")?.asString ?: ""
            val nodeId = jsonObj.get("node_id")?.asString ?: "unknown"
            val signature = jsonObj.get("signature")?.asString ?: ""

            val metric = MetricEntity(
                id = id,
                ts = ts,
                nodeId = nodeId,
                metricJson = metricJson,
                signature = signature
            )
            metricDao.insertMetric(metric)
            Log.d(TAG, "Inserted metric: $id")
            Result.success(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting metric", e)
            Result.failure(e)
        }
    }

    suspend fun getMetricsSince(since: String, limit: Int): List<Map<String, Any>> {
        return try {
            val metrics = metricDao.getMetricsSince(since, limit)
            metrics.map { metric ->
                val metricJson = gson.fromJson(metric.metricJson, Map::class.java) as? Map<String, Any> ?: emptyMap()
                mapOf(
                    "id" to metric.id,
                    "ts" to metric.ts,
                    "node_id" to metric.nodeId,
                    "metric" to metricJson
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting metrics", e)
            emptyList()
        }
    }

    suspend fun getMetricById(id: String): MetricEntity? {
        return try {
            metricDao.getMetricById(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting metric", e)
            null
        }
    }

    suspend fun getMetricCount(): Int {
        return try {
            metricDao.getMetricCount()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting metric count", e)
            0
        }
    }

    suspend fun deleteMetricsBefore(before: String) {
        try {
            metricDao.deleteMetricsBefore(before)
            Log.d(TAG, "Deleted metrics before $before")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting metrics", e)
        }
    }

    companion object {
        private const val TAG = "MetricRepository"
    }
}
