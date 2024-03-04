package com.keelim.mygrade.ui.screen.grade.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.core.database.model.Notices
import com.keelim.core.data.source.note.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    val noteRepository: NoteRepository,
) : ViewModel() {

    val notesUiState = noteRepository
        .getNoteList()
        .mapLatest {
            it.getOrNull() ?: emptyList()
        }
        .asSealedUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SealedUiState.loading())

    fun deleteNote(note: Notices) {
        viewModelScope.launch {
            noteRepository.deleteNoteList(note)
        }
    }
}
