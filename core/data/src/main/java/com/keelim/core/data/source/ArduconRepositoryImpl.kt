package com.keelim.core.data.source

import com.keelim.core.database.repository.ArduconDataSource
import com.keelim.core.database.repository.ArduconRepository
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import com.keelim.model.DeepLink
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArduconRepositoryImpl @Inject constructor(
    private val local: ArduconDataSource,
    @Dispatcher(KeelimDispatchers.IO) private val dispatcher: CoroutineDispatcher,
) : ArduconRepository {
    override suspend fun insertDeepLinkUrl(deepLink: DeepLink) {
        withContext(dispatcher) {
            local.insertDeepLinkUrl(deepLink)
        }
    }

    override fun getDeepLinkUrls(keyword: String) = if (keyword.isEmpty()) {
        local.getDeepLinkUrls()
    } else {
        local.getDeepLinkUrlsFiltered(keyword)
    }

    override suspend fun deleteDeepLinkUrl(deepLink: DeepLink) {
        withContext(dispatcher) {
            local.deleteDeepLinkUrl(deepLink)
        }
    }

    override suspend fun updateDeepLinkUrl(deepLink: DeepLink) {
        withContext(dispatcher) {
            local.updateDeepLinkUrl(deepLink)
        }
    }

    override suspend fun insertScheme(scheme: String) {
        withContext(dispatcher) {
            local.insertScheme(scheme)
        }
    }

    override suspend fun deleteScheme(scheme: String) {
        withContext(dispatcher) {
            local.insertScheme(scheme)
        }
    }

    override fun getSchemeList(): Flow<List<String>> = local.getSchemeList()
}
