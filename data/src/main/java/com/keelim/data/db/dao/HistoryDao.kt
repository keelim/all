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
    suspend fun insertHistories(vararg histories: History)

    @Update
    suspend fun updateHistories(vararg histories: History)

    @Delete
    suspend fun deleteHistories(vararg histories: History)

    @Query("SELECT * FROM history WHERE uid = :uid")
    fun loadHistoriesById(uid: Int): Flow<History>

    @Query("SELECT * FROM history WHERE subject IN (:subjects)")
    fun loadHistoriesBySubjects(subjects: List<String>): Flow<List<History>>

    @Query("SELECT * FROM history")
    fun getAll(): Flow<List<History>>
}