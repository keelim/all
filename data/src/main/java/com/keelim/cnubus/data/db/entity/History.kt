package com.keelim.cnubus.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    val destination: String,
    val root:String,
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0
)