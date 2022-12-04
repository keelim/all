package com.keelim.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "history")
data class History(
    var subject: String,
    var origin: Int,
    var average: Float,
    var std: Float,
    var number: Int,
    var grade_num: Float,
    var grade: String,
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
)

@Entity(tableName = "simple_history")
data class SimpleHistory(
    val name: String,
    val date: String = Calendar.getInstance().time.toString(),
    val grade: String,
    val rank: String,
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
)

@Entity
data class NandaHistory(
    @PrimaryKey val uid: Int?, @ColumnInfo(name = "keyword") val keyword: String?
)
