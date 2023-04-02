/*
 * Designed and developed by 2023 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.data.di.network

import com.keelim.data.BuildConfig
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.isSuccess
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorNetworkModule {
    @KtorHttpClient
    @Singleton
    fun provideKtorClient(): HttpClient = HttpClient(CIO) {
        engine {
            maxConnectionsCount = 1000
            endpoint {
                // this: EndpointConfig
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
        }
        install(Resources) {

        }
        install(DefaultRequest) {

        }
        install(UserAgent) {
            agent = "Ktor client"
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
            maxRetries = 5
            retryIf { request, response ->
                !response.status.isSuccess()
            }
            retryOnExceptionIf { request, cause ->
                // cause is NetworkError
                false
            }
            delayMillis { retry ->
                retry * 3000L
            }
            modifyRequest { request ->
               // request.headers.append("x-retry-count", retryCount.toString())
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 1000L
            connectTimeoutMillis=  10_000L
        }
        if (BuildConfig.DEBUG) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
        }
    }

    @KtorWebsocketHttpClient
    @Singleton
    fun provideKtorWebsocketHttpClient(): HttpClient = HttpClient {
        install(WebSockets)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KtorHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KtorWebsocketHttpClient
}
