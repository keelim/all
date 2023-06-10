package com.keelim.data.source

import com.keelim.data.source.local.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeAll(): Flow<List<History>>
    suspend fun create(history: History): String
    suspend fun complete(historyId: String, grade: String)
    suspend fun refresh()
}
