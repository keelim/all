package com.keelim.core.data.source.local

import com.keelim.core.database.dao.ArduconDao
import com.keelim.core.database.model.DeepLink
import javax.inject.Inject

class ArduconDataSourceImpl @Inject constructor(
    private val dao: ArduconDao
) : ArduconDataSource {
    override suspend fun insertDeepLinkUrl(deepLink: DeepLink) = dao.insertDeepLinkUrl(deepLink)
    override fun getDeepLinkUrls() = dao.getDeepLinkUrls()
    override suspend fun deleteDeepLinkUrl(deepLink: DeepLink) = dao.deleteDeepLinkUrl(deepLink)
}
