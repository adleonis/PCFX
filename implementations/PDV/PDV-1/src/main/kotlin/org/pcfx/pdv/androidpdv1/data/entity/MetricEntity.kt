package org.pcfx.pdv.androidpdv1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metrics")
data class MetricEntity(
    @PrimaryKey
    val id: String,
    val ts: String,
    val nodeId: String,
    val schema: String = "pcfx.metric/0.1",
    val metricJson: String,
    val createdAt: Long = System.currentTimeMillis(),
    val signature: String = ""
)
