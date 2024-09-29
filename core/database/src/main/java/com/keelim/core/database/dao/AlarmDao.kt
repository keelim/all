package com.keelim.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.core.database.model.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Upsert
    suspend fun upsert(alarm: AlarmEntity)

    @Query("SELECT * FROM alarm")
    fun getAllAlarms(): Flow<List<AlarmEntity>>
}
