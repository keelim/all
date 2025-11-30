package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "base64_history")
data class Base64History(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val text: String,
    val isEncoded: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
)
