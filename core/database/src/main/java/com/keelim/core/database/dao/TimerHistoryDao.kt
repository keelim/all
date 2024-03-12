package com.keelim.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.core.database.model.TimerHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerHistoryDao {
    @Query("SELECT * FROM timerHistory")
    fun observeAll(): Flow<List<TimerHistory>>

    @Upsert
    suspend fun upsert(history: TimerHistory)

    // 1 means true
    @Query("UPDATE timerHistory SET isCompleted = 1 WHERE uid = :historyId")
    suspend fun updateCompleted(historyId: String)

    @Query("DELETE FROM timerHistory")
    suspend fun deleteAll()
}
