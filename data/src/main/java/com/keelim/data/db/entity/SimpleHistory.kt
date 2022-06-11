package com.keelim.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity(tableName = "simple_history")
data class SimpleHistory(
    val date: Date = Calendar.getInstance().time,
    val grade: String,
    val rank: String,
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
)
