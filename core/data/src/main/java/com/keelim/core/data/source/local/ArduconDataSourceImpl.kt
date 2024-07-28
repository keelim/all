package com.keelim.core.data.source.local

import com.keelim.core.database.dao.ArduconDao
import com.keelim.core.database.mapper.toDeepLink
import com.keelim.core.database.mapper.toDeepLinkEntity
import com.keelim.core.database.repository.ArduconDataSource
import com.keelim.model.DeepLink
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArduconDataSourceImpl @Inject constructor(
    private val dao: ArduconDao,
) : ArduconDataSource {
    override suspend fun insertDeepLinkUrl(deepLink: DeepLink) = dao.insertDeepLinkUrl(deepLink.toDeepLinkEntity())
    override fun getDeepLinkUrls() = dao.getDeepLinkUrls().map { it.toDeepLink() }
    override fun getDeepLinkUrlsFiltered(keyword: String) = dao.getDeepLinkUrlsFiltered(keyword).map { it.toDeepLink() }
    override suspend fun deleteDeepLinkUrl(deepLink: DeepLink) = dao.deleteDeepLinkUrl(deepLink.toDeepLinkEntity())
    override suspend fun updateDeepLinkUrl(deepLink: DeepLink) = dao.updateDeepLink(deepLink.toDeepLinkEntity())
}
