package com.keelim.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.data.source.local.LocalTask
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun observeAll(): Flow<List<LocalTask>>

    @Upsert
    suspend fun upsert(task: LocalTask)

    @Upsert
    suspend fun upsertAll(tasks: List<LocalTask>)

    @Query("UPDATE task SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)

    @Query("DELETE FROM task")
    suspend fun deleteAll()
}
