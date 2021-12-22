package com.keelim.data.repository

import com.keelim.data.db.entity.History
import kotlinx.coroutines.flow.Flow

interface IoRepository {
    // production
    suspend fun insertHistories(history: History)

    suspend fun updateHistories(history: History)

    suspend fun deleteHistories(history: History)

    fun loadHistoriesById(uid: Int): Flow<History>

    fun loadHistoriesBySubjects(subject: String): Flow<List<History>>

    val all: Flow<List<History>>
}