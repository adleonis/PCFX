package org.pcfx.client.c1.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.pcfx.client.c1.data.network.PDVClient
import org.pcfx.client.c1.data.preferences.SettingsPreferences
import javax.inject.Inject

data class ActivityItem(
    val id: String,
    val ts: String,
    val type: String,
    val title: String,
    val subtitle: String,
    val details: Map<String, Any>
)

data class ActivityFeedState(
    val items: List<ActivityItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastRefresh: Long = 0
)

@HiltViewModel
class ActivityFeedViewModel @Inject constructor(
    private val pdvClient: PDVClient,
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {
    
    private val _state = MutableStateFlow(ActivityFeedState())
    val state: StateFlow<ActivityFeedState> = _state.asStateFlow()
    
    private var autoRefreshJob: Job? = null
    private val lastTimestamp = MutableStateFlow("1970-01-01T00:00:00Z")

    init {
        observeSettings()
        loadActivity()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsPreferences.autoRefreshEnabledFlow.collect { enabled ->
                if (enabled) {
                    startAutoRefresh()
                } else {
                    stopAutoRefresh()
                }
            }
        }
    }

    fun loadActivity() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                
                val eventsResult = pdvClient.getEvents(since = lastTimestamp.value, limit = 10)
                val atomsResult = pdvClient.getAtoms(since = lastTimestamp.value, limit = 10)
                
                val items = mutableListOf<ActivityItem>()
                
                eventsResult.onSuccess { eventResponse ->
                    eventResponse.events.forEach { event ->
                        @Suppress("UNCHECKED_CAST")
                        val eventData = event["event"] as? Map<String, Any> ?: return@forEach
                        items.add(ActivityItem(
                            id = event["id"]?.toString() ?: "",
                            ts = event["ts"]?.toString() ?: "",
                            type = "event",
                            title = "${eventData["source"]?.toString()} captured ${eventData["content"]?.toString()}",
                            subtitle = event["device"]?.toString() ?: "Unknown device",
                            details = eventData
                        ))
                    }
                }
                
                atomsResult.onSuccess { atomResponse ->
                    atomResponse.atoms.forEach { atom ->
                        @Suppress("UNCHECKED_CAST")
                        val atomData = atom["atom"] as? Map<String, Any> ?: return@forEach
                        items.add(ActivityItem(
                            id = atom["id"]?.toString() ?: "",
                            ts = atom["ts"]?.toString() ?: "",
                            type = "atom",
                            title = atomData["text"]?.toString() ?: "Knowledge Insight",
                            subtitle = "From node ${atom["node_id"]?.toString()}",
                            details = atomData
                        ))
                    }
                }
                
                items.sortByDescending { it.ts }
                
                _state.value = _state.value.copy(
                    items = items,
                    isLoading = false,
                    lastRefresh = System.currentTimeMillis()
                )
                
                if (items.isNotEmpty()) {
                    lastTimestamp.value = items.firstOrNull()?.ts ?: lastTimestamp.value
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading activity", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun startAutoRefresh() {
        if (autoRefreshJob != null) return
        
        autoRefreshJob = viewModelScope.launch {
            while (true) {
                val interval = settingsPreferences.refreshIntervalFlow.collect { delay(it.toLong()) }
                loadActivity()
            }
        }
    }

    private fun stopAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = null
    }

    companion object {
        private const val TAG = "ActivityFeedViewModel"
    }
}
