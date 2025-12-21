@file:OptIn(ExperimentalObjCName::class)

package com.keelim.shared.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Entity(tableName = "shortened_url")
@ObjCName("ShortenedUrlEntity")
data class ShortenedUrlEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalUrl: String,
    val shortCode: String,
    val title: String = "",
    @ColumnInfo(defaultValue = "0") val clickCount: Int = 0,
    @ColumnInfo(defaultValue = "0") val createdAt: Long = 0L,
    @ColumnInfo(defaultValue = "0") val lastClickedAt: Long = 0L,
    @ColumnInfo(defaultValue = "0") val expiresAt: Long = 0L,
)
