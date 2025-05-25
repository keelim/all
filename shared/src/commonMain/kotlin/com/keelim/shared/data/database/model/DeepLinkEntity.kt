package com.keelim.shared.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deepLink")
data class DeepLinkEntity(
    @PrimaryKey val url: String,
    val timestamp: Long,
    @ColumnInfo(defaultValue = "0") val isBookMarked: Boolean = false,
    @ColumnInfo(defaultValue = "") val title: String = "",
)
