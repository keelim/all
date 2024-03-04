package com.keelim.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.core.database.model.NetworkCache

@Dao
interface NetworkCacheDao {
    @Upsert
    suspend fun upsertCache(dataCache: NetworkCache)

    @Query("SELECT * FROM network_cache WHERE url = :url")
    suspend fun getCache(url: String): NetworkCache?
}
