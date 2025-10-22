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

data class EventsState(
    val events: List<Map<String, Any>> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedContentKind: String? = null,
    val selectedSurface: String? = null,
    val contentKindOptions: List<String> = listOf("text", "audio", "image", "video", "ad", "system"),
    val surfaceOptions: List<String> = listOf("app", "browser", "audio", "tv", "wearable", "system")
)

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val pdvClient: PDVClient
) : ViewModel() {
    
    private val _state = MutableStateFlow(EventsState())
    val state: StateFlow<EventsState> = _state.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                
                val result = pdvClient.getEvents(limit = 50)
                
                result.onSuccess { response ->
                    _state.value = _state.value.copy(
                        events = response.events,
                        isLoading = false
                    )
                }.onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading events", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun filterByContentKind(kind: String?) {
        _state.value = _state.value.copy(selectedContentKind = kind)
    }

    fun filterBySurface(surface: String?) {
        _state.value = _state.value.copy(selectedSurface = surface)
    }

    fun getFilteredEvents(): List<Map<String, Any>> {
        return _state.value.events.filter { event ->
            val eventData = event["event"] as? Map<String, Any>
            val contentKind = eventData?.get("content") as? Map<String, Any>
            val contentKindValue = contentKind?.get("kind") as? String
            
            val sourceData = eventData?.get("source") as? Map<String, Any>
            val surfaceValue = sourceData?.get("surface") as? String
            
            val matchesKind = _state.value.selectedContentKind?.let { contentKindValue == it } != false
            val matchesSurface = _state.value.selectedSurface?.let { surfaceValue == it } != false
            
            matchesKind && matchesSurface
        }
    }

    companion object {
        private const val TAG = "EventsViewModel"
    }
}
