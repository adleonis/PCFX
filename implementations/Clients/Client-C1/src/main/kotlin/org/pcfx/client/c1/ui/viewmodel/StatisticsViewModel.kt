package org.pcfx.client.c1.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.pcfx.client.c1.data.network.PDVClient
import javax.inject.Inject

data class StatisticsState(
    val totalEvents: Int = 0,
    val totalAtoms: Int = 0,
    val totalMetrics: Int = 0,
    val eventsBySurface: Map<String, Int> = emptyMap(),
    val eventsByContentKind: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val pdvClient: PDVClient
) : ViewModel() {
    
    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                
                val statsResult = pdvClient.getStats()
                val eventsResult = pdvClient.getEvents(limit = 100)
                
                var totalEvents = 0
                var totalAtoms = 0
                var totalMetrics = 0
                val eventsBySurface = mutableMapOf<String, Int>()
                val eventsByContentKind = mutableMapOf<String, Int>()
                
                statsResult.onSuccess { stats ->
                    totalEvents = (stats["event_count"] as? Number)?.toInt() ?: 0
                    totalAtoms = (stats["atom_count"] as? Number)?.toInt() ?: 0
                    totalMetrics = (stats["metric_count"] as? Number)?.toInt() ?: 0
                }
                
                eventsResult.onSuccess { eventResponse ->
                    eventResponse.events.forEach { event ->
                        @Suppress("UNCHECKED_CAST")
                        val eventData = event["event"] as? Map<String, Any> ?: return@forEach
                        
                        val source = eventData["source"] as? Map<String, Any>
                        val surface = source?.get("surface") as? String ?: "unknown"
                        eventsBySurface[surface] = (eventsBySurface[surface] ?: 0) + 1
                        
                        val content = eventData["content"] as? Map<String, Any>
                        val kind = content?.get("kind") as? String ?: "unknown"
                        eventsByContentKind[kind] = (eventsByContentKind[kind] ?: 0) + 1
                    }
                }
                
                _state.value = _state.value.copy(
                    totalEvents = totalEvents,
                    totalAtoms = totalAtoms,
                    totalMetrics = totalMetrics,
                    eventsBySurface = eventsBySurface,
                    eventsByContentKind = eventsByContentKind,
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error loading statistics", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    companion object {
        private const val TAG = "StatisticsViewModel"
    }
}
