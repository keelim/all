package com.keelim.data.repository

import com.keelim.model.Notices
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNoteList(): Flow<Result<List<Notices>>>
    suspend fun getNoteDetail(id: Int): Result<Notices>
    suspend fun updateNote(notes: Notices): Result<Unit>
    suspend fun deleteNoteList(notes: Notices): Result<Unit>
}
