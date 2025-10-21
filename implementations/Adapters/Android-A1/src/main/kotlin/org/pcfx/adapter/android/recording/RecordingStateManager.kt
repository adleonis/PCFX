package org.pcfx.adapter.android.recording

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class RecordingState {
    object Idle : RecordingState()
    object Recording : RecordingState()
    object Paused : RecordingState()
    data class Error(val message: String, val exception: Throwable? = null) : RecordingState()
}

class RecordingStateManager {
    private val _state = MutableStateFlow<RecordingState>(RecordingState.Idle)
    val state: StateFlow<RecordingState> = _state.asStateFlow()

    fun setState(newState: RecordingState) {
        _state.value = newState
    }

    fun getCurrentState(): RecordingState = _state.value

    fun isRecording(): Boolean = _state.value is RecordingState.Recording

    fun isPaused(): Boolean = _state.value is RecordingState.Paused
}
