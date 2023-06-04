package com.keelim.data.source

import kotlinx.coroutines.flow.Flow

interface DefaultTaskRepository {
    fun observeAll(): Flow<List<Task>>
    suspend fun create(title: String, description: String): String
    suspend fun complete(taskId: String)
    suspend fun refresh()
}
