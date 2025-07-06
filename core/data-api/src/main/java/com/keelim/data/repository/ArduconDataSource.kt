package com.keelim.data.repository

import com.keelim.model.DeepLink
import com.keelim.model.UsageStat
import kotlinx.coroutines.flow.Flow

interface ArduconDataSource {
    suspend fun insertDeepLinkUrl(deepLink: DeepLink)
    fun getDeepLinkUrls(): Flow<List<DeepLink>>
    fun getDeepLinkUrlsFiltered(keyword: String): Flow<List<DeepLink>>
    suspend fun deleteDeepLinkUrl(deepLink: DeepLink)
    suspend fun updateDeepLinkUrl(deepLink: DeepLink)
    suspend fun insertScheme(scheme: String)

    suspend fun deleteScheme(scheme: String)

    fun getSchemeList(): Flow<List<String>>

    fun getCategories(): Flow<List<String>>

    suspend fun getTopUsedLinks(limit: Int): List<DeepLink>
    suspend fun getRecentUsedLinks(limit: Int): List<DeepLink>
    suspend fun getDailyUsageStats(limit: Int): List<UsageStat>
}
