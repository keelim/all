package com.keelim.core.data.source.local

import com.keelim.core.database.mapper.toDeepLink
import com.keelim.core.database.mapper.toDeepLinkEntity
import com.keelim.core.database.mapper.toPlain
import com.keelim.core.database.mapper.toSchemeEntity
import com.keelim.data.repository.ArduconDataSource
import com.keelim.model.DeepLink
import com.keelim.shared.data.database.dao.ArduconDao
import kotlinx.coroutines.flow.Flow
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
    override suspend fun insertScheme(scheme: String) = dao.insertScheme(scheme.toSchemeEntity())

    override suspend fun deleteScheme(scheme: String) = dao.deleteScheme(scheme)

    override fun getSchemeList(): Flow<List<String>> = dao.getSchemeList().map { it.toPlain() }

    override fun getCategories(): Flow<List<String>> = dao.getCategories()
}
