package org.pcfx.pdv.androidpdv1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val ts: String,
    val device: String,
    val adapterId: String,
    val schema: String = "pcfx.exposure_event/0.1",
    val eventJson: String,
    val createdAt: Long = System.currentTimeMillis(),
    val signature: String = ""
)
