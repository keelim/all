package com.keelim.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val title: String,
    val subTitle: String,
    val receiveDate: String,
)
