package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.keelim.shared.data.database.model.ShortenedUrlEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortenedUrlDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shortenedUrl: ShortenedUrlEntity): Long

    @Update
    suspend fun update(shortenedUrl: ShortenedUrlEntity)

    @Delete
    suspend fun delete(shortenedUrl: ShortenedUrlEntity)

    @Query("SELECT * FROM shortened_url ORDER BY createdAt DESC")
    fun getAll(): Flow<List<ShortenedUrlEntity>>

    @Query("SELECT * FROM shortened_url WHERE id = :id")
    suspend fun getById(id: Long): ShortenedUrlEntity?

    @Query("SELECT * FROM shortened_url WHERE shortCode = :shortCode")
    suspend fun getByShortCode(shortCode: String): ShortenedUrlEntity?

    @Query("UPDATE shortened_url SET clickCount = clickCount + 1, lastClickedAt = :timestamp WHERE id = :id")
    suspend fun incrementClickCount(id: Long, timestamp: Long)

    @Query("SELECT * FROM shortened_url ORDER BY clickCount DESC LIMIT :limit")
    fun getMostClicked(limit: Int): Flow<List<ShortenedUrlEntity>>

    @Query("DELETE FROM shortened_url WHERE expiresAt > 0 AND expiresAt < :currentTime")
    suspend fun deleteExpired(currentTime: Long)
}
