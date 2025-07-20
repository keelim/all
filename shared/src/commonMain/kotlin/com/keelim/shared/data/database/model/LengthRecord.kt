package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "length_record")
data class LengthRecord(
    @PrimaryKey val date: String, // LocalDate를 String으로 저장
    val length: Float
)