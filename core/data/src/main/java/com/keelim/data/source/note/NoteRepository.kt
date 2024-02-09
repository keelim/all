package com.keelim.data.source.note


import com.keelim.data.model.entity.Notices as NoteModel
import com.keelim.data.model.entity.Notices
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun getNoteList(): Flow<Result<List<NoteModel>>>
    suspend fun getNoteDetail(id: Int): Result<NoteModel>
    suspend fun updateNote(notes: NoteModel): Result<Unit>
    suspend fun deleteNoteList(notes: Notices): Result<Unit>
}
