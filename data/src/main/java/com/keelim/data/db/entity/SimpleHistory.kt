package com.keelim.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "simple_history")
data class SimpleHistory(
    val name: String,
    val date: String = Calendar.getInstance().time.toString(),
    val grade: String,
    val rank: String,
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
)
