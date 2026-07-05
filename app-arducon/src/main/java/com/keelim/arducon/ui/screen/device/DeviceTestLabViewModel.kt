package com.keelim.arducon.ui.screen.device

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class DeviceTestLabViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(DeviceTestLabUiState())
    val uiState: StateFlow<DeviceTestLabUiState> = _uiState.asStateFlow()

    fun markRunning(id: DeviceTestId) {
        updateResult(
            id = id,
            status = DeviceTestStatus.Running,
            message = DeviceTestMessage.Running,
        )
    }

    fun applyOutcome(
        id: DeviceTestId,
        outcome: DeviceTestOutcome,
    ) {
        updateResult(
            id = id,
            status = outcome.status,
            message = outcome.message,
            detail = outcome.detail,
        )
    }

    fun reset(id: DeviceTestId) {
        _uiState.update { state ->
            state.copy(
                results = state.results + (id to DeviceTestResult(id = id)),
            )
        }
    }

    private fun updateResult(
        id: DeviceTestId,
        status: DeviceTestStatus,
        message: DeviceTestMessage,
        detail: String = "",
    ) {
        _uiState.update { state ->
            state.copy(
                results = state.results + (
                    id to DeviceTestResult(
                        id = id,
                        status = status,
                        message = message,
                        detail = detail,
                    )
                    ),
            )
        }
    }
}
