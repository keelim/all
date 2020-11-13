package com.keelim.nandadiagnosis.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nanda")
data class NandaEntity(
        @PrimaryKey
        val nanda_id: Int,

        val reason: String,

        val diagnosis: String,

        val class_name: String,

        val domain_name: String
)