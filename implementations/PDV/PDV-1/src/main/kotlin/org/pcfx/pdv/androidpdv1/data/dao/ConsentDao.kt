package org.pcfx.pdv.androidpdv1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.pcfx.pdv.androidpdv1.data.entity.ConsentEntity

@Dao
interface ConsentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsent(consent: ConsentEntity)

    @Query("SELECT * FROM consents WHERE consentId = :consentId")
    suspend fun getConsentById(consentId: String): ConsentEntity?

    @Query("SELECT * FROM consents WHERE adapterId = :adapterId")
    suspend fun getConsentsByAdapterId(adapterId: String): List<ConsentEntity>

    @Query("SELECT * FROM consents WHERE nodeId = :nodeId")
    suspend fun getConsentsByNodeId(nodeId: String): List<ConsentEntity>

    @Query("SELECT * FROM consents WHERE expiresAt > datetime('now')")
    suspend fun getActiveConsents(): List<ConsentEntity>

    @Query("DELETE FROM consents WHERE expiresAt < datetime('now')")
    suspend fun deleteExpiredConsents()
}
