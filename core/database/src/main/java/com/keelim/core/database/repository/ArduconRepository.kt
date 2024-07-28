package com.keelim.core.database.repository

import com.keelim.model.DeepLink
import kotlinx.coroutines.flow.Flow

interface ArduconRepository {
    suspend fun insertDeepLinkUrl(deepLink: DeepLink)
    fun getDeepLinkUrls(keyword: String): Flow<List<DeepLink>>
    suspend fun deleteDeepLinkUrl(deepLink: DeepLink)
    suspend fun updateDeepLinkUrl(deepLink: DeepLink)
}
