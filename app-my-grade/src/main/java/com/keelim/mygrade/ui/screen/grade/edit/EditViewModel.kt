package com.keelim.mygrade.ui.screen.grade.edit

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.data.model.entity.Notices
import com.keelim.data.source.note.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber


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
): ViewModel() {

    private val _data = MutableStateFlow(EditUiState(
        editResult = EditResult.editResultInitial(savedStateHandle),
        descriptions = "",
    ))
    val data = _data.asStateFlow().asSealedUiState()
    fun updateNote() {
        val current = _data.value
        val title = current.editResult.subject
        val description = current.descriptions

        viewModelScope.launch {
            noteRepository.updateNote(
                Notices(
                    title = title,
                    note = description
                )
            ).onSuccess {
                _data.update { old ->
                    old.copy(
                        dialogState = EditDialogState.Success,
                    )
                }
            }.onFailure { throwable ->
                Timber.e(throwable.message)
                _data.update { old ->
                    old.copy(
                        dialogState = EditDialogState.Failed,
                    )
                }
            }
        }
    }
}
