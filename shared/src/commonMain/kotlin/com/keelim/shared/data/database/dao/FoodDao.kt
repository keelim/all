package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keelim.shared.data.database.model.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodEntity)

    @Query("SELECT * FROM food_record WHERE date = :date ORDER BY time DESC")
    fun getByDate(date: String): Flow<List<FoodEntity>>

    @Query("SELECT SUM(calories) FROM food_record WHERE date = :date")
    fun getTotalCaloriesByDate(date: String): Flow<Int?>

    @Query("DELETE FROM food_record WHERE id = :id")
    suspend fun deleteById(id: Long)
}
