package com.keelim.core.database.repository

import com.keelim.model.DeepLink
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
}
