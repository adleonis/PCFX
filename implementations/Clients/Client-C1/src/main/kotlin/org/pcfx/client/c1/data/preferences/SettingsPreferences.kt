package org.pcfx.client.c1.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.pcfx.client.c1.config.ClientBuildConfig
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("app_settings")

@Singleton
class SettingsPreferences @Inject constructor(private val context: Context) {

    companion object {
        private val PDV_HOST = stringPreferencesKey("pdv_host")
        private val PDV_PORT = intPreferencesKey("pdv_port")
        private val AUTO_REFRESH_ENABLED = stringPreferencesKey("auto_refresh_enabled")
        private val REFRESH_INTERVAL_MS = intPreferencesKey("refresh_interval_ms")
    }

    val pdvHostFlow: Flow<String> = context.dataStore.data.map { 
        it[PDV_HOST] ?: ClientBuildConfig.DEFAULT_PDV_HOST 
    }

    val pdvPortFlow: Flow<Int> = context.dataStore.data.map { 
        it[PDV_PORT] ?: ClientBuildConfig.DEFAULT_PDV_PORT 
    }

    val autoRefreshEnabledFlow: Flow<Boolean> = context.dataStore.data.map { 
        (it[AUTO_REFRESH_ENABLED] ?: "true").toBoolean()
    }

    val refreshIntervalFlow: Flow<Int> = context.dataStore.data.map { 
        it[REFRESH_INTERVAL_MS] ?: 5000 
    }

    suspend fun setPdvHost(host: String) {
        context.dataStore.edit { 
            it[PDV_HOST] = host 
        }
    }

    suspend fun setPdvPort(port: Int) {
        context.dataStore.edit { 
            it[PDV_PORT] = port 
        }
    }

    suspend fun setAutoRefreshEnabled(enabled: Boolean) {
        context.dataStore.edit { 
            it[AUTO_REFRESH_ENABLED] = enabled.toString()
        }
    }

    suspend fun setRefreshInterval(intervalMs: Int) {
        context.dataStore.edit { 
            it[REFRESH_INTERVAL_MS] = intervalMs
        }
    }
}
