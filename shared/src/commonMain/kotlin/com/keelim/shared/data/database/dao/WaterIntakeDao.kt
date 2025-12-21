package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keelim.model.DailyWaterTotal
import com.keelim.shared.data.database.model.WaterIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Query("SELECT * FROM water_intake WHERE date = :date ORDER BY timestamp DESC")
    fun getByDate(date: String): Flow<List<WaterIntake>>

    @Query("SELECT * FROM water_intake ORDER BY timestamp DESC")
    fun getAll(): Flow<List<WaterIntake>>

    @Query("SELECT SUM(amount) FROM water_intake WHERE date = :date")
    fun getTotalByDate(date: String): Flow<Int?>

    @Query("SELECT date, SUM(amount) as totalAmount FROM water_intake GROUP BY date ORDER BY date DESC LIMIT :days")
    fun getDailyTotals(days: Int): Flow<List<DailyWaterTotal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waterIntake: WaterIntake)

    @Query("DELETE FROM water_intake WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM water_intake WHERE date = :date")
    suspend fun deleteByDate(date: String)
}

