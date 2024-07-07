package com.keelim.core.data.source

import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.core.data.source.local.ArduconDataSource
import com.keelim.core.database.model.DeepLink
import kotlinx.coroutines.CoroutineDispatcher
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

    override fun getDeepLinkUrls() = local.getDeepLinkUrls()

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
}
