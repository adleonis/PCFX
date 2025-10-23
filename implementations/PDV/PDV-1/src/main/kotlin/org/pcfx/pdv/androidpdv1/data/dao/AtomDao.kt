package org.pcfx.pdv.androidpdv1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.pcfx.pdv.androidpdv1.data.entity.AtomEntity

@Dao
interface AtomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAtom(atom: AtomEntity)

    @Query("SELECT * FROM atoms WHERE ts > :since ORDER BY ts ASC LIMIT :limit")
    suspend fun getAtomsSince(since: String, limit: Int): List<AtomEntity>

    @Query("SELECT * FROM atoms WHERE id = :id")
    suspend fun getAtomById(id: String): AtomEntity?

    @Query("SELECT * FROM atoms ORDER BY ts DESC LIMIT :limit")
    suspend fun getRecentAtoms(limit: Int): List<AtomEntity>

    @Query("SELECT * FROM atoms ORDER BY ts DESC LIMIT :limit OFFSET :offset")
    suspend fun getRecentAtomsWithOffset(limit: Int, offset: Int): List<AtomEntity>

    @Query("SELECT COUNT(*) FROM atoms")
    suspend fun getAtomCount(): Int

    @Query("DELETE FROM atoms WHERE ts < :before")
    suspend fun deleteAtomsBefore(before: String)
}
