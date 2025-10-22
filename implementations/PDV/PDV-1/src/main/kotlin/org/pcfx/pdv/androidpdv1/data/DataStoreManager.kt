package org.pcfx.pdv.androidpdv1.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object PdvDataStoreManager {
    @Volatile
    private var instance: DataStore<Preferences>? = null

    private val Context.pdvDataStore: DataStore<Preferences> by preferencesDataStore(name = "pdv_preferences")

    fun getDataStore(context: Context): DataStore<Preferences> {
        return instance ?: synchronized(this) {
            instance ?: context.pdvDataStore.also { instance = it }
        }
    }
}
