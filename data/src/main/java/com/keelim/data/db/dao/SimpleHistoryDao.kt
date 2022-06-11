package com.keelim.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.keelim.data.db.entity.SimpleHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SimpleHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistories(history: SimpleHistory)

    @Update
    suspend fun updateHistories(history: SimpleHistory)

    @Delete
    suspend fun deleteHistories(history: SimpleHistory)

    @Query("SELECT * FROM simple_history WHERE uid = :uid")
    fun loadHistoriesById(uid: Int): Flow<SimpleHistory>

    @Query("SELECT * FROM simple_history")
    fun getAll(): Flow<List<SimpleHistory>>

    @Query("SELECT * FROM simple_history LIMIT :loadSize OFFSET (:page-1)*:loadSize")
    suspend fun getPagingAll(page:Int, loadSize:Int): List<SimpleHistory>

    @Query("SELECT * FROM simple_history")
    suspend fun getAllSimpleHistory(): List<SimpleHistory>
}
