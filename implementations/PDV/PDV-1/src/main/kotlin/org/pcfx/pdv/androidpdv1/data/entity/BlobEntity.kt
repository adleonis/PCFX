package org.pcfx.pdv.androidpdv1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blobs")
data class BlobEntity(
    @PrimaryKey
    val hash: String,
    val contentType: String,
    val size: Long,
    val fileName: String,
    val createdAt: Long = System.currentTimeMillis(),
    val retentionDays: Int = 30
)
