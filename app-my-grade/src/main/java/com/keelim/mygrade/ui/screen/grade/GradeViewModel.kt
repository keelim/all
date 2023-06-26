package com.keelim.mygrade.ui.screen.grade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.model.GradeResult
import com.keelim.data.source.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val historyRepository: HistoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GradeUiState.empty())
    val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()

    private val _data = MutableStateFlow(GradeResult.gradeResultInitial(savedStateHandle))
    val data: StateFlow<GradeResult> = _data.asStateFlow()

    init {
        updateMessage()
    }

    private fun updateMessage() {
        val gradeResult = GradeResult.gradeResultInitial(savedStateHandle)
        viewModelScope.launch {
            historyRepository.create(
                grade = gradeResult.grade,
                point = gradeResult.point
            ).also {
                if(it) {
                    _uiState.update { old ->
                        old.copy(
                            isMessageShow = true,
                            message = "처리 완료되었습니다. ",
                        )
                    }
                }
            }
        }
    }

    fun dismissMessage() {
        _uiState.update { old ->
            old.copy(
                isMessageShow = false,
                message = "",
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
                message = "",
            )
        }
    }
}
