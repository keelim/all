package com.keelim.data.repository

interface CacheRepository {
    suspend fun getResponse(url: String, enforce: Boolean = false): String
}
