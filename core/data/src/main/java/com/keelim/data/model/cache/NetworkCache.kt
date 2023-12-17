package com.keelim.data.model.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_cache")
data class NetworkCache(
    @PrimaryKey val url: String,
    val json: String,
    val timestamp: Long
)
