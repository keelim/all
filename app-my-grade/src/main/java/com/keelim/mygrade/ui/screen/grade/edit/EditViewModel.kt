package com.keelim.mygrade.ui.screen.grade.edit

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.core.database.repository.NoteRepository
import com.keelim.core.database.model.Notices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject

@Parcelize
data class EditResult(
    val subject: String,
) : Parcelable {
    companion object {
        fun editResultInitial(savedStateHandle: SavedStateHandle): EditResult {
            return EditResult(
                subject = checkNotNull(savedStateHandle["subject"]),
            )
        }
    }
}

@Immutable
data class EditUiState(
    val editResult: EditResult,
    val descriptions: String,
    val dialogState: EditDialogState = EditDialogState.IDLE,
)

enum class EditDialogState {
    Success,
    Failed,
    IDLE,
}

@HiltViewModel
class EditViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val noteRepository: NoteRepository,
) : ViewModel() {

    private val _data = MutableStateFlow(
        EditUiState(
            editResult = EditResult.editResultInitial(savedStateHandle),
            descriptions = "",
        ),
    )
    val data = _data.asSealedUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SealedUiState.Loading)
    fun updateNote(descriptions: String) {
        val current = _data.value
        val title = current.editResult.subject

        viewModelScope.launch {
            noteRepository.updateNote(
                Notices(
                    title = title,
                    note = descriptions,
                ),
            ).onSuccess {
                _data.update { old ->
                    old.copy(
                        dialogState = EditDialogState.Success,
                        descriptions = descriptions,
                    )
                }
            }.onFailure { throwable ->
                Timber.e(throwable.message)
                _data.update { old ->
                    old.copy(
                        dialogState = EditDialogState.Failed,
                        descriptions = descriptions,
                    )
                }
            }
        }
    }

    fun clearDialogState() {
        _data.update { old ->
            old.copy(
                dialogState = EditDialogState.IDLE,
            )
        }
    }
}
