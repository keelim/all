@file:OptIn(ExperimentalObjCName::class)

package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Entity(tableName = "network_cache")
@ObjCName("network_cache")
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
