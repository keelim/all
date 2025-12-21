package com.keelim.shared.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@Entity(tableName = "studySession")
@ObjCName("studySession")
data class StudySession(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(defaultValue = "") val subject: String = "",
    val durationSeconds: Int = 0,
    val date: String = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.toString(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
)
