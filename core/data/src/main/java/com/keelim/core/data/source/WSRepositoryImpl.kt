package com.keelim.core.data.source

import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.core.network.di.KtorNetworkModule
import com.keelim.data.repository.WSRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class WSRepositoryImpl @Inject constructor(
    @Dispatcher(KeelimDispatchers.IO) private val io: CoroutineDispatcher,
    @KtorNetworkModule.KtorWebsocketHttpClient private val client: HttpClient,
) : WSRepository {
    override suspend fun connect(host: String) {
        client.webSocket(
            host = host,
            port = 8080,
        ) {
            // TODO: if you want
        }
    }

    override suspend fun disconnect() {
        client.close()
    }
}
