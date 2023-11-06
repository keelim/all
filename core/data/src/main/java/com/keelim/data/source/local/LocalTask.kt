package com.keelim.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "task",
)
data class LocalTask(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    var isCompleted: Boolean,
    @ColumnInfo(name = "date", defaultValue = "") val date: String,
)
