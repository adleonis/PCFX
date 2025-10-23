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
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val selectedContentKind: String? = null,
    val selectedSurface: String? = null,
    val contentKindOptions: List<String> = listOf("text", "audio", "image", "video", "ad", "system"),
    val surfaceOptions: List<String> = listOf("app", "browser", "audio", "tv", "wearable", "system"),
    val hasMoreEvents: Boolean = true
)

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val pdvClient: PDVClient
) : ViewModel() {

    private val _state = MutableStateFlow(EventsState())
    val state: StateFlow<EventsState> = _state.asStateFlow()

    private val pageSize = 10
    private var currentOffset = 0
    private var lastLoadedCount = 0

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                Log.i(TAG, "========== VIEWMODEL LOAD EVENTS START ==========")
                Log.i(TAG, "Loading events from PDV server...")

                currentOffset = 0
                val result = pdvClient.getRecentEvents(limit = pageSize, offset = 0)

                result.onSuccess { response ->
                    Log.i(TAG, "\n========== VIEWMODEL RECEIVED RESPONSE ==========")
                    Log.i(TAG, "Successfully loaded ${response.count} events")

                    // Log detailed info about each event
                    response.events.forEachIndexed { index, event ->
                        Log.i(TAG, "\n--- Event #$index ---")
                        val eventId = event["id"] as? String ?: "unknown"
                        Log.i(TAG, "Event ID: $eventId")

                        @Suppress("UNCHECKED_CAST")
                        val eventData = event["event"] as? Map<String, Any>
                        @Suppress("UNCHECKED_CAST")
                        val content = eventData?.get("content") as? Map<String, Any>
                        val textField = content?.get("text") as? String ?: ""
                        val contentKind = content?.get("kind") as? String ?: "unknown"

                        Log.i(TAG, "Content Kind: $contentKind")
                        Log.i(TAG, "Text Field Present: ${textField.isNotEmpty()}")
                        Log.i(TAG, "Text Length: ${textField.length}")
                        if (textField.isNotEmpty()) {
                            Log.i(TAG, "Text Preview: ${textField.substring(0, minOf(80, textField.length))}...")
                        } else {
                            Log.w(TAG, "⚠️ Text field is EMPTY or NULL!")
                        }
                    }

                    lastLoadedCount = response.count
                    _state.value = _state.value.copy(
                        events = response.events,
                        isLoading = false,
                        hasMoreEvents = response.count >= pageSize
                    )
                    currentOffset = pageSize
                    Log.i(TAG, "========== VIEWMODEL LOAD EVENTS SUCCESS ==========")
                }.onFailure { e ->
                    Log.e(TAG, "✗ Failed to load events: ${e.message}", e)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "✗ Error loading events", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadMoreEvents() {
        if (_state.value.isLoadingMore || !_state.value.hasMoreEvents) {
            return
        }

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoadingMore = true)
                Log.i(TAG, "Loading more events, offset=$currentOffset")

                val result = pdvClient.getRecentEvents(limit = pageSize, offset = currentOffset)

                result.onSuccess { response ->
                    Log.i(TAG, "Successfully loaded ${response.count} more events")
                    lastLoadedCount = response.count
                    _state.value = _state.value.copy(
                        events = _state.value.events + response.events,
                        isLoadingMore = false,
                        hasMoreEvents = response.count >= pageSize
                    )
                    currentOffset += pageSize
                }.onFailure { e ->
                    Log.e(TAG, "Failed to load more events: ${e.message}", e)
                    _state.value = _state.value.copy(
                        isLoadingMore = false,
                        error = e.message
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading more events", e)
                _state.value = _state.value.copy(
                    isLoadingMore = false,
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
