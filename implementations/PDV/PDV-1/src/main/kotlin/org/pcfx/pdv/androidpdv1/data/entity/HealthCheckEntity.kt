package org.pcfx.pdv.androidpdv1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_checks")
data class HealthCheckEntity(
    @PrimaryKey
    val appId: String,
    val appType: String,
    val appName: String,
    val appVersion: String,
    val platformInfo: String,
    val firstConnection: Long,
    val lastConnected: Long,
    val connectionCount: Int = 1
)
