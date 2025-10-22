package org.pcfx.pdv.androidpdv1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consents")
data class ConsentEntity(
    @PrimaryKey
    val consentId: String,
    val adapterId: String? = null,
    val nodeId: String? = null,
    val capsJson: String,
    val consentJson: String,
    val expiresAt: String,
    val createdAt: Long = System.currentTimeMillis(),
    val signature: String = ""
)
