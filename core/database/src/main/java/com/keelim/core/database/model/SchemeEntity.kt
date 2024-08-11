package com.keelim.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheme")
data class SchemeEntity(
    @PrimaryKey
    val url: String,
    val timestamp: Long,
)
