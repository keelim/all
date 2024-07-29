package com.keelim.core.data.source.note

import com.keelim.common.di.IoDispatcher
import com.keelim.core.database.dao.NoteDao
import com.keelim.core.database.mapper.toNotices
import com.keelim.core.database.mapper.toNoticesEntity
import com.keelim.data.repository.NoteRepository
import com.keelim.model.Notices
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
) : NoteRepository {
    override fun getNoteList(): Flow<Result<List<Notices>>> =
        noteDao.getNotes()
            .mapLatest { it.toNotices() }
            .mapLatest { runCatching { it } }

    override suspend fun getNoteDetail(id: Int): Result<Notices> =
        runCatching {
            withContext(dispatcher) {
                noteDao.getNoteDetail(id).toNotices()
            }
        }

    override suspend fun updateNote(notes: Notices): Result<Unit> =
        runCatching {
            withContext(dispatcher) {
                noteDao.updateNote(notes.toNoticesEntity())
            }
        }

    override suspend fun deleteNoteList(notes: Notices): Result<Unit> =
        runCatching {
            withContext(dispatcher) {
                noteDao.deleteNotes(notes.toNoticesEntity())
            }
        }
}
