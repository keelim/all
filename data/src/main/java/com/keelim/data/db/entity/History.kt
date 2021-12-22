package com.keelim.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey
    var uid: Int,
    var subject: String,
    var origin: Int,
    var average: Float,
    var std: Float,
    var number: Int,
    var grade_num: Float,
    var grade: String
)