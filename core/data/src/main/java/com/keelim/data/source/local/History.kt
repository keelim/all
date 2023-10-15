package com.keelim.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

@Entity(tableName = "simpleHistory")
data class SimpleHistory(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val subject: String = "not supported",
    val date: String = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString(),
    val grade: String,
    val gradeRank: Int,
    val totalRank: Int
)

@Entity(tableName = "timerHistory")
data class TimerHistory(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val date: String = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString(),
    val historyTime: String = "",
    val isCompleted: Boolean = false,
)
