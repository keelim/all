package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keelim.shared.data.database.model.Base64History
import kotlinx.coroutines.flow.Flow

@Dao
interface Base64Dao {
    @Query("SELECT * FROM base64_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Base64History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: Base64History)

    @Delete
    suspend fun delete(history: Base64History)

    @Query("DELETE FROM base64_history")
    suspend fun deleteAll()
}
