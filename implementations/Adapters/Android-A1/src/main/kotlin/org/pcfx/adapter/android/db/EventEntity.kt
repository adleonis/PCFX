package org.pcfx.adapter.android.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exposure_events")
data class EventEntity(
    @PrimaryKey
    val id: String,

    val schema: String,
    val ts: String,
    val device: String,
    val adapterId: String,
    val capabilitiesUsed: String, // JSON array as string
    val sourceJson: String,
    val contentJson: String,
    val privacyJson: String,
    val signature: String,

    val eventJson: String, // Full JSON for POST
    val isPosted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastAttemptAt: Long? = null,
    val attemptCount: Int = 0
)
