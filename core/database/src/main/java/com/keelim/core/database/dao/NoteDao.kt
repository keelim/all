package com.keelim.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.core.database.model.NoticesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notices")
    fun getNotes(): Flow<List<NoticesEntity>>

    @Query("SELECT * FROM notices WHERE uid = :id")
    fun getNoteDetail(id: Int): NoticesEntity

    @Upsert
    fun updateNote(notes: NoticesEntity)

    @Delete
    fun deleteNotes(notes: NoticesEntity): Int
}
