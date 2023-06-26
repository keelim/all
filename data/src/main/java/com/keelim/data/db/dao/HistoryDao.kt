package com.keelim.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.data.source.local.History
import com.keelim.data.source.local.SimpleHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun observeAll(): Flow<List<History>>

    @Query("SELECT * FROM simpleHistory")
    fun observeSimpleHistories(): Flow<List<SimpleHistory>>

    @Upsert
    suspend fun upsert(history: History)

    @Upsert
    suspend fun upsertSimpleHistory(history: SimpleHistory)

    @Upsert
    suspend fun upsertAll(histories: List<History>)

    @Query("UPDATE history SET grade = :grade WHERE uid = :historyId")
    suspend fun updateCompleted(historyId: String, grade: String)

    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Query("SELECT * FROM history LIMIT :loadSize OFFSET (:page-1)*:loadSize")
    suspend fun getPagingAll(page: Int, loadSize: Int): List<History>
}
