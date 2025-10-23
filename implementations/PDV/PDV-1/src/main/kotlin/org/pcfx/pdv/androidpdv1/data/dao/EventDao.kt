package org.pcfx.pdv.androidpdv1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.pcfx.pdv.androidpdv1.data.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("SELECT * FROM events WHERE ts > :since ORDER BY ts ASC LIMIT :limit")
    suspend fun getEventsSince(since: String, limit: Int): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    @Query("SELECT * FROM events ORDER BY ts DESC LIMIT :limit")
    suspend fun getRecentEvents(limit: Int): List<EventEntity>

    @Query("SELECT * FROM events ORDER BY ts DESC LIMIT :limit OFFSET :offset")
    suspend fun getRecentEventsWithOffset(limit: Int, offset: Int): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int

    @Query("DELETE FROM events WHERE ts < :before")
    suspend fun deleteEventsBefore(before: String)
}
