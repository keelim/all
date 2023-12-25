package com.keelim.data.model.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock

@Entity(tableName = "network_cache")
data class NetworkCache(
    @PrimaryKey val url: String,
    val json: String,
    val timestamp: Long,
) {
    companion object {
        fun newInstance(
            url: String,
            body: String,
        ): NetworkCache = NetworkCache(
            url = url,
            json = body,
            timestamp = Clock.System.now().toEpochMilliseconds(),
        )
    }
}
