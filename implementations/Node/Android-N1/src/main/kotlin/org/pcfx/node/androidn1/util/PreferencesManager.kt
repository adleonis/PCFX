package org.pcfx.node.androidn1.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.Instant

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pcfx_node_prefs")

class PreferencesManager(private val context: Context) {
    private val pdvBaseUrlKey = stringPreferencesKey("pdv_base_url")
    private val pdvCapabilityTokenKey = stringPreferencesKey("pdv_capability_token")
    private val scheduleEnabledKey = booleanPreferencesKey("schedule_enabled")
    private val scheduleTimeKey = stringPreferencesKey("schedule_time")
    private val lastWatermarkKey = stringPreferencesKey("last_watermark")
    private val lastRunTimeKey = stringPreferencesKey("last_run_time")
    private val lastRunStatusKey = stringPreferencesKey("last_run_status")
    private val lastRunEventsCountKey = stringPreferencesKey("last_run_events_count")
    private val lastRunAtomsCountKey = stringPreferencesKey("last_run_atoms_count")
    private val lastRunErrorKey = stringPreferencesKey("last_run_error")

    suspend fun setPdvBaseUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[pdvBaseUrlKey] = url
        }
    }

    fun getPdvBaseUrl(): String {
        return try {
            val prefs = context.dataStore.data
            var url = ""
            // Blocking read for initialization - in production use coroutines
            runBlocking {
                url = prefs.map { it[pdvBaseUrlKey] ?: "http://127.0.0.1:7777" }.first()
            }
            url
        } catch (e: Exception) {
            "http://127.0.0.1:7777"
        }
    }

    fun getPdvBaseUrlFlow(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[pdvBaseUrlKey] ?: "http://127.0.0.1:7777"
        }
    }

    suspend fun setPdvCapabilityToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[pdvCapabilityTokenKey] = token
        }
    }

    fun getPdvCapabilityToken(): String {
        return try {
            var token = ""
            runBlocking {
                token = context.dataStore.data.map { it[pdvCapabilityTokenKey] ?: "" }.first()
            }
            token
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun setScheduleEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[scheduleEnabledKey] = enabled
        }
    }

    fun isScheduleEnabled(): Boolean {
        return try {
            var enabled = false
            runBlocking {
                enabled = context.dataStore.data.map { it[scheduleEnabledKey] ?: false }.first()
            }
            enabled
        } catch (e: Exception) {
            false
        }
    }

    fun isScheduleEnabledFlow(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[scheduleEnabledKey] ?: false
        }
    }

    suspend fun setScheduleTime(hourOfDay: Int, minuteOfHour: Int) {
        val timeString = String.format("%02d:%02d", hourOfDay, minuteOfHour)
        context.dataStore.edit { preferences ->
            preferences[scheduleTimeKey] = timeString
        }
    }

    fun getScheduleTime(): Pair<Int, Int> {
        return try {
            var timeString = ""
            runBlocking {
                timeString = context.dataStore.data.map { it[scheduleTimeKey] ?: "09:00" }.first()
            }
            val parts = timeString.split(":")
            Pair(parts[0].toInt(), parts[1].toInt())
        } catch (e: Exception) {
            Pair(9, 0)
        }
    }

    fun getScheduleTimeFlow(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[scheduleTimeKey] ?: "09:00"
        }
    }

    suspend fun setLastWatermark(timestamp: String) {
        context.dataStore.edit { preferences ->
            preferences[lastWatermarkKey] = timestamp
        }
    }

    fun getLastWatermark(): String {
        return try {
            var watermark = ""
            runBlocking {
                watermark = context.dataStore.data.map { it[lastWatermarkKey] ?: Instant.EPOCH.toString() }.first()
            }
            watermark
        } catch (e: Exception) {
            Instant.EPOCH.toString()
        }
    }

    suspend fun recordRunResult(
        status: String,
        eventsCount: Int,
        atomsCount: Int,
        error: String? = null
    ) {
        context.dataStore.edit { preferences ->
            preferences[lastRunTimeKey] = Instant.now().toString()
            preferences[lastRunStatusKey] = status
            preferences[lastRunEventsCountKey] = eventsCount.toString()
            preferences[lastRunAtomsCountKey] = atomsCount.toString()
            if (error != null) {
                preferences[lastRunErrorKey] = error
            }
        }
    }

    fun getLastRunInfo(): RunInfo {
        return try {
            var info = RunInfo()
            runBlocking {
                context.dataStore.data.map { prefs ->
                    info = RunInfo(
                        lastRunTime = prefs[lastRunTimeKey],
                        status = prefs[lastRunStatusKey] ?: "never",
                        eventsCount = prefs[lastRunEventsCountKey]?.toIntOrNull() ?: 0,
                        atomsCount = prefs[lastRunAtomsCountKey]?.toIntOrNull() ?: 0,
                        error = prefs[lastRunErrorKey]
                    )
                }.first()
            }
            info
        } catch (e: Exception) {
            RunInfo()
        }
    }

    fun getLastRunInfoFlow(): Flow<RunInfo> {
        return context.dataStore.data.map { prefs ->
            RunInfo(
                lastRunTime = prefs[lastRunTimeKey],
                status = prefs[lastRunStatusKey] ?: "never",
                eventsCount = prefs[lastRunEventsCountKey]?.toIntOrNull() ?: 0,
                atomsCount = prefs[lastRunAtomsCountKey]?.toIntOrNull() ?: 0,
                error = prefs[lastRunErrorKey]
            )
        }
    }

    data class RunInfo(
        val lastRunTime: String? = null,
        val status: String = "never",
        val eventsCount: Int = 0,
        val atomsCount: Int = 0,
        val error: String? = null
    )
}
