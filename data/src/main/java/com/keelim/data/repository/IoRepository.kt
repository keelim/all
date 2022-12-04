package com.keelim.data.repository

import androidx.paging.PagingData
import com.keelim.data.db.entity.History
import com.keelim.data.db.entity.SimpleHistory
import kotlinx.coroutines.flow.Flow

interface IoRepository {
    // production
    suspend fun insertHistories(history: History)

    suspend fun updateHistories(history: History)

    suspend fun deleteHistories(history: History)

    suspend fun getAllHistories(): List<History>

    fun loadHistoriesById(uid: Int): Flow<History>

    fun loadHistoriesBySubjects(subject: String): Flow<List<History>>

    val all: Flow<List<History>>
    val simpleAll: Flow<List<SimpleHistory>>

    fun getAllPaging(): Flow<PagingData<History>>
    suspend fun insertSimpleHistories(history: SimpleHistory)
    suspend fun getAllSimpleHistories(history: SimpleHistory): List<SimpleHistory>
}
