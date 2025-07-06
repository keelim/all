@file:OptIn(ExperimentalObjCName::class)

package com.keelim.shared.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Entity(tableName = "deepLink")
@ObjCName("DeepLinkEntity")
data class DeepLinkEntity(
    @PrimaryKey val timestamp: Long,
    val url: String,
    @ColumnInfo(defaultValue = "0") val isBookMarked: Boolean = false,
    @ColumnInfo(defaultValue = "") val title: String = "",
    @ColumnInfo(defaultValue = "") val category: String = "",
    @ColumnInfo(defaultValue = "0") val usageCount: Int = 0,
    @ColumnInfo(defaultValue = "0") val lastUsed: Long = 0L,
)

data class UsageStatEntity(
    val day: String,
    val count: Int,
)
