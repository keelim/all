package com.keelim.core.database.repository

import com.keelim.shared.data.database.model.LocalTask
import kotlinx.coroutines.flow.Flow

interface DefaultTaskRepository {
    fun observeAll(): Flow<List<LocalTask>>
    suspend fun create()
    suspend fun create(title: String, description: String): String
    suspend fun complete(taskId: String)
    suspend fun upsert(task: LocalTask)

    fun delete(task: LocalTask)
    suspend fun refresh()

    fun clear()
}
