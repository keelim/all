package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.shared.data.database.model.TimerHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerHistoryDao {
    @Query("SELECT * FROM timerHistory ORDER BY date DESC")
    fun observeAll(): Flow<List<TimerHistory>>

    @Upsert
    suspend fun upsert(history: TimerHistory)

    @Query("UPDATE timerHistory SET isCompleted = 1 WHERE uid = :historyId")
    suspend fun updateCompleted(historyId: Int)

    @Query("UPDATE timerHistory SET description = :description WHERE uid = :historyId")
    suspend fun updateDescription(historyId: Int, description: String)

    @Query("DELETE FROM timerHistory WHERE uid = :historyId")
    suspend fun deleteById(historyId: Int)

    @Query("DELETE FROM timerHistory")
    suspend fun deleteAll()
}
