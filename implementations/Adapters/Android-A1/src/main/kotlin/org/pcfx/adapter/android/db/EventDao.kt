package org.pcfx.adapter.android.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("SELECT * FROM exposure_events WHERE isPosted = 0 ORDER BY createdAt ASC LIMIT :limit")
    suspend fun getUnpostedEvents(limit: Int = 32): List<EventEntity>

    @Query("SELECT * FROM exposure_events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    @Query("SELECT COUNT(*) FROM exposure_events WHERE isPosted = 0")
    suspend fun getUnpostedCount(): Int

    @Query("DELETE FROM exposure_events WHERE isPosted = 1 AND createdAt < :olderThanMs")
    suspend fun deletePostedEventsOlderThan(olderThanMs: Long)

    @Query("SELECT * FROM exposure_events ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentEvents(limit: Int = 100): List<EventEntity>
}
