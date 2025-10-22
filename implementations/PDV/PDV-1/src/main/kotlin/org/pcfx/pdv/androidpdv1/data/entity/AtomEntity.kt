package org.pcfx.pdv.androidpdv1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "atoms")
data class AtomEntity(
    @PrimaryKey
    val id: String,
    val ts: String,
    val nodeId: String,
    val schema: String = "pcfx.knowledge_atom/0.1",
    val atomJson: String,
    val createdAt: Long = System.currentTimeMillis(),
    val signature: String = ""
)
