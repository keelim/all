package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntake(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Int, // ml 단위
    val timestamp: Long, // System.currentTimeMillis()
    val date: String, // LocalDate를 String으로 저장 (yyyy-MM-dd)
)
