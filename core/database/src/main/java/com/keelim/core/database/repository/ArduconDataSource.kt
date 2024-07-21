package com.keelim.core.database.repository

import com.keelim.core.database.model.DeepLink
import kotlinx.coroutines.flow.Flow

interface ArduconDataSource {
    suspend fun insertDeepLinkUrl(deepLink: DeepLink)
    fun getDeepLinkUrls(): Flow<List<DeepLink>>
    suspend fun deleteDeepLinkUrl(deepLink: DeepLink)
    suspend fun updateDeepLinkUrl(deepLink: DeepLink)
}
