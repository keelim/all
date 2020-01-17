package com.keelim.nandadiagnosis.mainfragment.search.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "nanda")
data class NandaEntity(
        @PrimaryKey
        val nanda_id: Int,

        val reason: String,

        val diagnosis: String,

        val class_name: String,

        val domain_name: String
)