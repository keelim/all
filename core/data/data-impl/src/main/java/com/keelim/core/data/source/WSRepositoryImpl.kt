package com.keelim.core.data.source

import com.keelim.core.network.di.KtorNetworkModule
import com.keelim.data.repository.WSRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.close
import jakarta.inject.Inject

class WSRepositoryImpl @Inject constructor(
    @KtorNetworkModule.KtorWebsocketHttpClient private val client: HttpClient,
) : WSRepository {
    @Volatile
    private var activeSession: DefaultClientWebSocketSession? = null

    override suspend fun connect(host: String) {
        client.webSocket(
            host = host,
            port = 8080,
        ) {
            activeSession = this
            try {
                closeReason.await()
            } finally {
                if (activeSession == this) {
                    activeSession = null
                }
            }
        }
    }

    override suspend fun disconnect() {
        val session = activeSession
        activeSession = null
        session?.close()
    }
}
