package com.keelim.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.keelim.data.db.entity.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistories(history: History)

    @Update
    suspend fun updateHistories(history: History)

    @Delete
    suspend fun deleteHistories(history: History)

    @Query("SELECT * FROM history WHERE uid = :uid")
    fun loadHistoriesById(uid: Int): Flow<History>

    @Query("SELECT * FROM history WHERE subject IN (:subject)")
    fun loadHistoriesBySubjects(subject: String): Flow<List<History>>

    @Query("SELECT * FROM history")
    fun getAll(): Flow<List<History>>

    @Query("SELECT * FROM history LIMIT :loadSize OFFSET (:page-1)*:loadSize")
    suspend fun getPagingAll(page: Int, loadSize: Int): List<History>

    @Query("SELECT * FROM history")
    suspend fun getAllHistory(): List<History>
}
