package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.shared.data.database.model.NetworkCache

@Dao
interface NetworkCacheDao {
    @Upsert
    suspend fun upsertCache(dataCache: NetworkCache)

    @Query("SELECT * FROM network_cache WHERE url = :url")
    suspend fun getCache(url: String): NetworkCache?
}
