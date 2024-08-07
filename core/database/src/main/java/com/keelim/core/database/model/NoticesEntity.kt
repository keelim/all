package com.keelim.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Notices")
data class NoticesEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "created_at") val createdAt: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(name = "updated_at") val updatedAt: Date = Date(System.currentTimeMillis()),
)
