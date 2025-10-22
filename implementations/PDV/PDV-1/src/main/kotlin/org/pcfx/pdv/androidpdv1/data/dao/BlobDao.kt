package org.pcfx.pdv.androidpdv1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.pcfx.pdv.androidpdv1.data.entity.BlobEntity

@Dao
interface BlobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlob(blob: BlobEntity)

    @Query("SELECT * FROM blobs WHERE hash = :hash")
    suspend fun getBlobByHash(hash: String): BlobEntity?

    @Query("SELECT COUNT(*) FROM blobs")
    suspend fun getBlobCount(): Int

    @Query("DELETE FROM blobs WHERE hash = :hash")
    suspend fun deleteBlob(hash: String)

    @Query("SELECT * FROM blobs")
    suspend fun getAllBlobs(): List<BlobEntity>
}
