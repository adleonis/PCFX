package org.pcfx.pdv.androidpdv1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.pcfx.pdv.androidpdv1.data.dao.AtomDao
import org.pcfx.pdv.androidpdv1.data.dao.BlobDao
import org.pcfx.pdv.androidpdv1.data.dao.ConsentDao
import org.pcfx.pdv.androidpdv1.data.dao.EventDao
import org.pcfx.pdv.androidpdv1.data.dao.MetricDao
import org.pcfx.pdv.androidpdv1.data.entity.AtomEntity
import org.pcfx.pdv.androidpdv1.data.entity.BlobEntity
import org.pcfx.pdv.androidpdv1.data.entity.ConsentEntity
import org.pcfx.pdv.androidpdv1.data.entity.EventEntity
import org.pcfx.pdv.androidpdv1.data.entity.MetricEntity

@Database(
    entities = [
        EventEntity::class,
        AtomEntity::class,
        MetricEntity::class,
        BlobEntity::class,
        ConsentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PdvDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun atomDao(): AtomDao
    abstract fun metricDao(): MetricDao
    abstract fun blobDao(): BlobDao
    abstract fun consentDao(): ConsentDao

    companion object {
        @Volatile
        private var INSTANCE: PdvDatabase? = null

        fun getInstance(context: Context): PdvDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PdvDatabase::class.java,
                    "pcfx_pdv.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
