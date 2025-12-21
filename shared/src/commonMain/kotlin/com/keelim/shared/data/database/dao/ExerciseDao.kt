package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keelim.shared.data.database.model.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercise_record WHERE date = :date ORDER BY time DESC")
    fun getByDate(date: String): Flow<List<ExerciseEntity>>

    @Query("DELETE FROM exercise_record WHERE id = :id")
    suspend fun deleteById(id: Long)
}
