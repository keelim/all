package com.keelim.mygrade.ui.screen.grade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.keelim.data.model.GradeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(GradeUiState.empty())
    val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()


    private val _data = MutableStateFlow(GradeResult.gradeResultInitial(savedStateHandle))
    val data : StateFlow<GradeResult> = _data.asStateFlow()


    fun updateMessage() {
        _uiState.update { old ->
            old.copy(
                isMessageShow = true,
                message = "처리 완료되었습니다. "
            )
        }
    }

    fun dismissMessage() {
        _uiState.update { old ->
            old.copy(
                isMessageShow = false,
                message = ""
            )
        }
    }

    data class GradeUiState(
        val isMessageShow: Boolean,
        val message: String,
    ) {
        companion object {
            fun empty() = GradeUiState(
                isMessageShow = false,
                message = ""
            )
        }
    }
}
