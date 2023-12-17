package com.keelim.data.source.cache

interface CacheRepository {
    suspend fun getResponse(url: String, enforce: Boolean = false): String
}
