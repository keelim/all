@file:OptIn(ExperimentalObjCName::class)

package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Entity(tableName = "scheme")
@ObjCName("scheme")

data class SchemeEntity(
    @PrimaryKey
    val url: String,
    val timestamp: Long,
)
