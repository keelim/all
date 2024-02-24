package com.keelim.comssa.ui.screen.main.flash

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
@Stable
@HiltViewModel
class FlashCardViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FlashUiState())
    val uiState: StateFlow<FlashUiState> = _uiState.asStateFlow()

    fun updateState() {
        _uiState.update { old ->
            old.copy(
                flashCardState = old.flashCardState.nextFace(),
            )
        }
    }
}

data class FlashUiState(
    val flashCardState: FlashCardState = FlashCardState.Front,
)
sealed class FlashCardState(val angle: Float) {
    abstract fun nextFace(): FlashCardState
    data object Front : FlashCardState(angle = 0f) {
        override fun nextFace(): FlashCardState = Back
    }
    data object Back : FlashCardState(angle = 180f) {
        override fun nextFace(): FlashCardState = Front
    }
}
