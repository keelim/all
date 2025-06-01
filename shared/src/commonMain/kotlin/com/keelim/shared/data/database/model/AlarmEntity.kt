@file:OptIn(ExperimentalObjCName::class)

package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Entity(tableName = "alarm")
@ObjCName("AlarmEntity")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val title: String,
    val subTitle: String,
    val receiveDate: String,
)
