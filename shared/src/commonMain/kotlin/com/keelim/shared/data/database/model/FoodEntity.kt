package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_record")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val calories: Int,
    val date: String, // YYYY-MM-DD
    val time: Long
)
