package com.keelim.data.source.note

import com.keelim.core.database.dao.NoteDao
import com.keelim.core.database.model.Notices
import com.keelim.data.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
) : NoteRepository {
    override fun getNoteList(): Flow<Result<List<Notices>>> =
        noteDao.getNotes()
            .mapLatest { runCatching { it } }

    override suspend fun getNoteDetail(id: Int): Result<Notices> =
        runCatching {
            withContext(dispatcher) {
                noteDao.getNoteDetail(id)
            }
        }

    override suspend fun updateNote(notes: Notices): Result<Unit> =
        runCatching {
            withContext(dispatcher) {
                noteDao.updateNote(notes)
            }
        }

    override suspend fun deleteNoteList(notes: Notices): Result<Unit> =
        runCatching {
            withContext(dispatcher) {
                noteDao.deleteNotes(notes)
            }
        }
}
