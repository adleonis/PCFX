package org.pcfx.client.c1.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.pcfx.client.c1.config.ClientBuildConfig
import org.pcfx.client.c1.data.network.PDVClient
import org.pcfx.client.c1.data.preferences.SettingsPreferences
import org.pcfx.client.c1.network.PDVHealthCheckClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

enum class HealthCheckStatus {
    CHECKING, HEALTHY, UNHEALTHY, UNKNOWN
}

data class SettingsState(
    val pdvHost: String = ClientBuildConfig.DEFAULT_PDV_HOST,
    val pdvPort: Int = ClientBuildConfig.DEFAULT_PDV_PORT,
    val autoRefreshEnabled: Boolean = true,
    val refreshIntervalMs: Int = 5000,
    val searchQuery: String = "",
    val searchResults: List<Map<String, Any>> = emptyList(),
    val isSearching: Boolean = false,
    val appId: String = ClientBuildConfig.UNIQUE_APP_ID,
    val appVersion: String = ClientBuildConfig.APP_VERSION,
    val successMessage: String? = null,
    val healthCheckStatus: HealthCheckStatus = HealthCheckStatus.UNKNOWN,
    val healthCheckMessage: String? = null,
    val isCheckingHealth: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferences,
    private val pdvClient: PDVClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        loadSettings()
        performHealthCheck()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsPreferences.pdvHostFlow.collect { host ->
                _state.value = _state.value.copy(pdvHost = host)
            }
        }
        
        viewModelScope.launch {
            settingsPreferences.pdvPortFlow.collect { port ->
                _state.value = _state.value.copy(pdvPort = port)
            }
        }
        
        viewModelScope.launch {
            settingsPreferences.autoRefreshEnabledFlow.collect { enabled ->
                _state.value = _state.value.copy(autoRefreshEnabled = enabled)
            }
        }
        
        viewModelScope.launch {
            settingsPreferences.refreshIntervalFlow.collect { interval ->
                _state.value = _state.value.copy(refreshIntervalMs = interval)
            }
        }
    }

    fun updatePdvHost(host: String) {
        viewModelScope.launch {
            settingsPreferences.setPdvHost(host)
            pdvClient.setServer(host, _state.value.pdvPort)
            _state.value = _state.value.copy(successMessage = "PDV host updated")
        }
    }

    fun updatePdvPort(port: Int) {
        viewModelScope.launch {
            settingsPreferences.setPdvPort(port)
            pdvClient.setServer(_state.value.pdvHost, port)
            _state.value = _state.value.copy(successMessage = "PDV port updated")
        }
    }

    fun updateAutoRefresh(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setAutoRefreshEnabled(enabled)
        }
    }

    fun updateRefreshInterval(intervalMs: Int) {
        viewModelScope.launch {
            settingsPreferences.setRefreshInterval(intervalMs)
        }
    }

    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        if (query.isNotEmpty()) {
            performSearch(query)
        } else {
            _state.value = _state.value.copy(searchResults = emptyList())
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isSearching = true)
                
                val atomsResult = pdvClient.getAtoms(limit = 100)
                
                val results = mutableListOf<Map<String, Any>>()
                
                atomsResult.onSuccess { response ->
                    response.atoms.forEach { atom ->
                        @Suppress("UNCHECKED_CAST")
                        val atomData = atom["atom"] as? Map<String, Any> ?: return@forEach
                        val text = atomData["text"] as? String ?: ""
                        
                        if (text.contains(query, ignoreCase = true)) {
                            results.add(atom)
                        }
                    }
                }
                
                _state.value = _state.value.copy(
                    searchResults = results,
                    isSearching = false
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error performing search", e)
                _state.value = _state.value.copy(isSearching = false)
            }
        }
    }

    fun clearMessage() {
        _state.value = _state.value.copy(successMessage = null)
    }

    fun performHealthCheck() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isCheckingHealth = true,
                    healthCheckStatus = HealthCheckStatus.CHECKING
                )
                Log.i(TAG, "Performing health check...")

                val healthCheckClient = PDVHealthCheckClient.getInstance(context)
                val isHealthy = healthCheckClient.sendHealthCheck(
                    pdvHost = _state.value.pdvHost,
                    pdvPort = _state.value.pdvPort
                )

                if (isHealthy) {
                    _state.value = _state.value.copy(
                        healthCheckStatus = HealthCheckStatus.HEALTHY,
                        healthCheckMessage = "PDV server is healthy and reachable",
                        isCheckingHealth = false
                    )
                    Log.i(TAG, "Health check successful")
                } else {
                    _state.value = _state.value.copy(
                        healthCheckStatus = HealthCheckStatus.UNHEALTHY,
                        healthCheckMessage = "PDV server is not responding",
                        isCheckingHealth = false
                    )
                    Log.w(TAG, "Health check failed")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    healthCheckStatus = HealthCheckStatus.UNHEALTHY,
                    healthCheckMessage = "Error: ${e.message}",
                    isCheckingHealth = false
                )
                Log.e(TAG, "Health check error", e)
            }
        }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}
