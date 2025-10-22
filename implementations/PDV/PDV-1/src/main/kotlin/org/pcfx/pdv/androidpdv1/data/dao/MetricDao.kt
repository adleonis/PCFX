package org.pcfx.pdv.androidpdv1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.pcfx.pdv.androidpdv1.data.entity.MetricEntity

@Dao
interface MetricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: MetricEntity)

    @Query("SELECT * FROM metrics WHERE ts > :since ORDER BY ts ASC LIMIT :limit")
    suspend fun getMetricsSince(since: String, limit: Int): List<MetricEntity>

    @Query("SELECT * FROM metrics WHERE id = :id")
    suspend fun getMetricById(id: String): MetricEntity?

    @Query("SELECT COUNT(*) FROM metrics")
    suspend fun getMetricCount(): Int

    @Query("DELETE FROM metrics WHERE ts < :before")
    suspend fun deleteMetricsBefore(before: String)
}
