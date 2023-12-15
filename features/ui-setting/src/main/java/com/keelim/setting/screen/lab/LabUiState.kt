package com.keelim.setting.screen.lab

import androidx.compose.runtime.Stable

@Stable
sealed interface LabUiState {

    data object Initial : LabUiState

    data object Loading : LabUiState

    data class Success(
        val outputText: String
    ) : LabUiState

    data class Error(
        val errorMessage: String
    ) : LabUiState
}
