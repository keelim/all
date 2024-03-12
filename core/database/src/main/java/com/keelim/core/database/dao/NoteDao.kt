package com.keelim.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.core.database.model.Notices
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notices")
    fun getNotes(): Flow<List<Notices>>

    @Query("SELECT * FROM notices WHERE uid = :id")
    fun getNoteDetail(id: Int): Notices

    @Upsert
    fun updateNote(notes: Notices)

    @Delete
    fun deleteNotes(notes: Notices): Int
}
