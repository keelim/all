package com.keelim.nandadiagnosis.mainfragment.search.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nanda")
class Diagnosis(
        @PrimaryKey
        @ColumnInfo(name = "nanda_id")
        val id: Int,

        @ColumnInfo(name = "reason")
        val reason: String,

        @ColumnInfo(name = "diagnosis")
        val diagnosis: String,

        @ColumnInfo(name = "class_name")
        val class_name: String,

        @ColumnInfo(name = "domain_name")
        val domain_name: String

)