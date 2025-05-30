package com.keelim.core.database.repository

import com.keelim.shared.data.database.model.History
import com.keelim.shared.data.database.model.SimpleHistory
import com.keelim.shared.data.database.model.TimerHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeAll(): Flow<List<History>>
    fun observeSimpleHistories(): Flow<List<SimpleHistory>>

    fun observeTimerHistories(): Flow<List<TimerHistory>>

    suspend fun create(history: History): String
    suspend fun create(subject: String, grade: String, point: String): Boolean

    suspend fun complete(historyId: String, grade: String)

    suspend fun completedTimerHistory(historyId: String)
    suspend fun refresh()
}
