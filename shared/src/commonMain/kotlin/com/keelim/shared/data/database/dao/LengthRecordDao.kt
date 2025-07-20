package com.keelim.shared.data.database.dao

import androidx.room.*
import com.keelim.shared.data.database.model.LengthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface LengthRecordDao {
    @Query("SELECT * FROM length_record ORDER BY date ASC")
    fun getAll(): Flow<List<LengthRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: LengthRecord)

    @Query("DELETE FROM length_record WHERE date = :date")
    suspend fun deleteByDate(date: String)
} 