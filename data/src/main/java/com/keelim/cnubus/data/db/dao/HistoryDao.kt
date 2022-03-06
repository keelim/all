package com.keelim.cnubus.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keelim.cnubus.data.db.entity.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM History")
    fun getHistoryAll(): Flow<List<History>>

    @Query("SELECT * FROM History")
    suspend fun getHistoryRawAll(): List<History>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Delete
    suspend fun deleteHistory(history: History)

    @Query("DELETE FROM History")
    suspend fun deleteAll()
}