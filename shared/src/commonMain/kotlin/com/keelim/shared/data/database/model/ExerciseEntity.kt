package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_record")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val duration: String,
    val date: String, // YYYY-MM-DD
    val time: Long
)
