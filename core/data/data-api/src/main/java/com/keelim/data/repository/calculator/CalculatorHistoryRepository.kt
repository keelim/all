package com.keelim.data.repository.calculator

import com.keelim.model.CalculatorHistory
import kotlinx.coroutines.flow.Flow

interface CalculatorHistoryRepository {
    fun getAllHistory(): Flow<List<CalculatorHistory>>
    suspend fun addHistory(history: CalculatorHistory)
    suspend fun clearHistory()
}
