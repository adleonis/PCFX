package org.pcfx.pdv.androidpdv1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.pcfx.pdv.androidpdv1.data.entity.HealthCheckEntity

@Dao
interface HealthCheckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthCheck(healthCheck: HealthCheckEntity)

    @Update
    suspend fun updateHealthCheck(healthCheck: HealthCheckEntity)

    @Query("SELECT * FROM health_checks WHERE appId = :appId")
    suspend fun getHealthCheckByAppId(appId: String): HealthCheckEntity?

    @Query("SELECT * FROM health_checks")
    suspend fun getAllHealthChecks(): List<HealthCheckEntity>

    @Query("SELECT * FROM health_checks WHERE appType = :appType")
    suspend fun getHealthChecksByType(appType: String): List<HealthCheckEntity>

    @Query("SELECT COUNT(*) FROM health_checks")
    suspend fun getHealthCheckCount(): Int

    @Query("SELECT COUNT(*) FROM health_checks WHERE appType = :appType")
    suspend fun getHealthCheckCountByType(appType: String): Int

    @Query("SELECT COUNT(*) FROM health_checks WHERE lastConnected > :timestamp")
    suspend fun getActiveHealthChecksSince(timestamp: Long): Int

    @Query("SELECT COUNT(*) FROM health_checks WHERE appType = :appType AND lastConnected > :timestamp")
    suspend fun getActiveHealthChecksByTypeSince(appType: String, timestamp: Long): Int

    @Query("DELETE FROM health_checks WHERE appId = :appId")
    suspend fun deleteHealthCheckByAppId(appId: String)
}
