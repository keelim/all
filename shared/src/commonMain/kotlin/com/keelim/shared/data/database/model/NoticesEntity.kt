@file:OptIn(ExperimentalObjCName::class)

package com.keelim.shared.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Entity(tableName = "Notices")
@ObjCName("Notices")
data class NoticesEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)
