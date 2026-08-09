package com.keelim.core.data.repository

import com.keelim.shared.data.database.model.Base64History
import kotlinx.coroutines.flow.Flow

interface Base64Repository {
    fun getAllHistory(): Flow<List<Base64History>>
    suspend fun insertHistory(text: String, isEncoded: Boolean)
    suspend fun deleteHistory(history: Base64History)
    suspend fun deleteAllHistory()
}
