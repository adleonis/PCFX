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

data class EntityInfo(
    val id: String,
    val type: String,
    val name: String,
    val count: Int = 1
)

data class InsightsState(
    val atoms: List<Map<String, Any>> = emptyList(),
    val topEntities: List<EntityInfo> = emptyList(),
    val sentimentDistribution: Map<String, Int> = emptyMap(),
    val averageConfidence: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val pdvClient: PDVClient
) : ViewModel() {
    
    private val _state = MutableStateFlow(InsightsState())
    val state: StateFlow<InsightsState> = _state.asStateFlow()

    init {
        loadInsights()
    }

    fun loadInsights() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                
                val result = pdvClient.getAtoms(limit = 50)
                
                result.onSuccess { response ->
                    val atoms = response.atoms
                    val entities = mutableMapOf<String, EntityInfo>()
                    val sentiments = mutableMapOf<String, Int>()
                    var totalConfidence = 0.0
                    var confidentAtoms = 0
                    
                    atoms.forEach { atom ->
                        @Suppress("UNCHECKED_CAST")
                        val atomData = atom["atom"] as? Map<String, Any> ?: return@forEach
                        
                        val atomEntities = atomData["entities"] as? List<Map<String, Any>> ?: emptyList()
                        atomEntities.forEach { entity ->
                            val entityId = entity["id"] as? String ?: return@forEach
                            val entityType = entity["type"] as? String ?: "UNKNOWN"
                            val entityName = entity["name"] as? String ?: "Unknown"
                            
                            entities[entityId] = entities[entityId]?.let {
                                it.copy(count = it.count + 1)
                            } ?: EntityInfo(entityId, entityType, entityName, 1)
                        }
                        
                        val tone = atomData["tone"] as? Map<String, Any>
                        val sentiment = (tone?.get("sentiment") as? String)?.lowercase() ?: "neutral"
                        sentiments[sentiment] = (sentiments[sentiment] ?: 0) + 1
                        
                        val confidence = atomData["confidence"] as? Map<String, Any>
                        val confidenceScore = confidence?.get("extraction_confidence") as? Number
                        if (confidenceScore != null) {
                            totalConfidence += confidenceScore.toDouble()
                            confidentAtoms++
                        }
                    }
                    
                    val avgConfidence = if (confidentAtoms > 0) totalConfidence / confidentAtoms else 0.0
                    val sortedEntities = entities.values.sortedByDescending { it.count }.take(10)
                    
                    _state.value = _state.value.copy(
                        atoms = atoms,
                        topEntities = sortedEntities,
                        sentimentDistribution = sentiments,
                        averageConfidence = avgConfidence,
                        isLoading = false
                    )
                }.onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading insights", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    companion object {
        private const val TAG = "InsightsViewModel"
    }
}
