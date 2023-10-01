package com.keelim.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.data.source.local.History
import com.keelim.data.source.local.SimpleHistory
import com.keelim.data.source.local.TimerHistory
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
