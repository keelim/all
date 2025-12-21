package com.keelim.data.repository

import com.keelim.model.SimpleHistory
import com.keelim.model.TimerHistoryModel
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeSimpleHistories(): Flow<List<SimpleHistory>>

    fun observeTimerHistories(): Flow<List<TimerHistoryModel>>

    suspend fun create(subject: String, grade: String, point: String): Boolean

    suspend fun complete(historyId: String, grade: String)

    suspend fun completedTimerHistory(historyId: Int)

    suspend fun deleteTimerHistory(historyId: Int)

    suspend fun deleteAllTimerHistories()

    suspend fun updateTimerHistoryDescription(historyId: Int, description: String)

    suspend fun createTimerHistory(hours: Int, minutes: Int, seconds: Int, description: String = "")

    suspend fun refresh()
}


