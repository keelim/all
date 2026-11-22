package com.keelim.data.repository

import com.keelim.model.DeepLink
import com.keelim.model.UsageStat
import kotlinx.coroutines.flow.Flow

interface ArduconRepository {
    suspend fun insertDeepLinkUrl(deepLink: DeepLink)
    fun getDeepLinkUrls(keyword: String = ""): Flow<List<DeepLink>>
    suspend fun deleteDeepLinkUrl(deepLink: DeepLink)
    suspend fun updateDeepLinkUrl(deepLink: DeepLink)
    suspend fun insertScheme(scheme: String)

    suspend fun deleteScheme(scheme: String)
    fun getSchemeList(): Flow<List<String>>

    fun getCategories(): Flow<List<String>>

    fun getTopUsedLinks(limit: Int): Flow<List<DeepLink>>
    fun getRecentUsedLinks(limit: Int): Flow<List<DeepLink>>
    fun getDailyUsageStats(limit: Int): Flow<List<UsageStat>>
}
