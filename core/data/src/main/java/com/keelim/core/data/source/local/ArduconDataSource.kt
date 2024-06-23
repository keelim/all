package com.keelim.core.data.source.local

import com.keelim.core.database.model.DeepLink
import kotlinx.coroutines.flow.Flow

interface ArduconDataSource {
    suspend fun insertDeepLinkUrl(deepLink: DeepLink)
    fun getDeepLinkUrls(): Flow<List<DeepLink>>
    suspend fun deleteDeepLinkUrl(deepLink: DeepLink)
}
