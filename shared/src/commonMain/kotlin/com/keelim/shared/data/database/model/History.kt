@file:OptIn(ExperimentalObjCName::class)

package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Entity(tableName = "history")
@ObjCName("history")
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
@ObjCName("simpleHistory")
data class SimpleHistory(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val subject: String = "not supported",
    val date: String = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString(),
    val grade: String,
    val gradeRank: Int,
    val totalRank: Int,
)

@Entity(tableName = "timerHistory")
@ObjCName("timerHistory")
data class TimerHistory(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val date: String = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString(),
    val historyTime: String = "",
    val isCompleted: Boolean = false,
)
