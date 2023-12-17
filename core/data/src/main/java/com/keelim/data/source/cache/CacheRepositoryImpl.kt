package com.keelim.data.source.cache

import com.keelim.data.db.dao.NetworkCacheDao
import com.keelim.data.di.network.KtorNetworkModule
import com.keelim.data.model.cache.NetworkCache
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import javax.inject.Inject

const val CACHE_VALIDITY = 60000L
class CacheRepositoryImpl @Inject constructor(
    val cacheDao: NetworkCacheDao,
    @KtorNetworkModule.KtorAndroidClient
    val client: HttpClient,
): CacheRepository {
    override suspend fun getResponse(url: String, enforce: Boolean): String {
        val cachedData = cacheDao.getCache(url)
        if (cachedData != null && System.currentTimeMillis() - cachedData.timestamp < CACHE_VALIDITY) {
            return cachedData.json
        } else {
            val responseBody = client.get(url).bodyAsText()
            cacheDao.upsertCache(NetworkCache.newInstance(url = url, body = responseBody))
            return responseBody
        }
    }
}
