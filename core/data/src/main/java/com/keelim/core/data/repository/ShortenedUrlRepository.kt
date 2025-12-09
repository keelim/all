package com.keelim.core.data.repository

import com.keelim.shared.data.database.model.ShortenedUrlEntity
import kotlinx.coroutines.flow.Flow

interface ShortenedUrlRepository {
    fun getAll(): Flow<List<ShortenedUrlEntity>>
    fun getMostClicked(limit: Int): Flow<List<ShortenedUrlEntity>>
    suspend fun getById(id: Long): ShortenedUrlEntity?
    suspend fun getByShortCode(shortCode: String): ShortenedUrlEntity?
    suspend fun insert(entity: ShortenedUrlEntity): Long
    suspend fun update(entity: ShortenedUrlEntity)
    suspend fun delete(entity: ShortenedUrlEntity)
    suspend fun incrementClickCount(id: Long, timestamp: Long)
    suspend fun deleteExpired(currentTime: Long)
}
