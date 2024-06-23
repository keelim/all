package com.keelim.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deepLink")
data class DeepLink(
    @PrimaryKey val url: String,
    val timestamp: Long,
)
