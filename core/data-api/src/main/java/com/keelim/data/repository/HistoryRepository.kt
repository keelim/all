package com.keelim.data.repository

import com.keelim.model.SimpleHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeSimpleHistories(): Flow<List<SimpleHistory>>

    suspend fun create(subject: String, grade: String, point: String): Boolean

    suspend fun complete(historyId: String, grade: String)

    suspend fun completedTimerHistory(historyId: String)
    
    suspend fun refresh()
}
