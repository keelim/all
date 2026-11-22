package com.keelim.core.data.source

import com.keelim.core.data.repository.ShortenedUrlRepository
import com.keelim.shared.data.database.dao.ShortenedUrlDao
import com.keelim.shared.data.database.model.ShortenedUrlEntity
import kotlinx.coroutines.flow.Flow
import jakarta.inject.Inject

class ShortenedUrlRepositoryImpl @Inject constructor(
    private val shortenedUrlDao: ShortenedUrlDao,
) : ShortenedUrlRepository {

    override fun getAll(): Flow<List<ShortenedUrlEntity>> = shortenedUrlDao.getAll()

    override fun getMostClicked(limit: Int): Flow<List<ShortenedUrlEntity>> =
        shortenedUrlDao.getMostClicked(limit)

    override suspend fun getById(id: Long): ShortenedUrlEntity? = shortenedUrlDao.getById(id)

    override suspend fun getByShortCode(shortCode: String): ShortenedUrlEntity? =
        shortenedUrlDao.getByShortCode(shortCode)

    override suspend fun insert(entity: ShortenedUrlEntity): Long = shortenedUrlDao.insert(entity)

    override suspend fun update(entity: ShortenedUrlEntity) = shortenedUrlDao.update(entity)

    override suspend fun delete(entity: ShortenedUrlEntity) = shortenedUrlDao.delete(entity)

    override suspend fun incrementClickCount(id: Long, timestamp: Long) =
        shortenedUrlDao.incrementClickCount(id, timestamp)

    override suspend fun deleteExpired(currentTime: Long) =
        shortenedUrlDao.deleteExpired(currentTime)
}
