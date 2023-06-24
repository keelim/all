package com.keelim.mygrade.ui.screen.quick

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.keelim.mygrade.ui.screen.main.Zvalue
import com.keelim.mygrade.ui.screen.main.getNormalProbability
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QuickAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var message by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
        private set

    private val _quickAddUiState = MutableStateFlow(QuickAddState.empty())
    val quickAddUiState: StateFlow<QuickAddState> = _quickAddUiState.asStateFlow()

    fun update(newMessage: TextFieldValue) {
        if (message.text.isEmpty() && newMessage.text.isEmpty().not()) {
            _quickAddUiState.tryEmit(
                QuickAddState.empty()
            )
        }
        message = newMessage
    }

    fun clear() {
        _quickAddUiState.tryEmit(
            QuickAddState.empty()
        )
    }

    fun submit() {
        if (message.text.count { it == ',' } != 3) {
            displayErrorMessage("올바른 양식으로 입력해주세요.")
            return
        }
        val items = message.text.split(",")
            .let { data ->
                runCatching {
                    require(data.size == 4) { "data length is always 4" }
                    data
                }.getOrElse {
                    emptyList()
                }
            }
        if (items.isEmpty()) {
            displayErrorMessage("알 수 없는 오류가 발생하였습니다. \n다시 한번 시도해주세요")
        } else {
            runCatching {
                val origin = items[0].trim().toFloat()
                val average = items[1].trim().toFloat()
                val number = items[2].trim().toFloat()
                val student = items[3].trim().toInt()
                Zvalue(((origin - average) / number).toDouble()).getNormalProbability() to student
            }.onSuccess {(normalProbability, student) ->
                _quickAddUiState.update {old ->
                    old.copy(
                        normalProbability = normalProbability,
                        student = student
                    )
                }
            }.onFailure {
                displayErrorMessage("입력 문구를 다시 한번 확인해주세요")
            }
        }
    }

    private fun displayErrorMessage(message: String) {
        _quickAddUiState.update { old ->
            old.copy(
                isError = true,
                errorMessage = message
            )
        }
    }
}
