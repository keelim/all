package com.keelim.cnubus.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(
    @PrimaryKey val comment_id: Int,
    @ColumnInfo(name="owner") val owner: String,
    @ColumnInfo(name="description") val description: String,
)