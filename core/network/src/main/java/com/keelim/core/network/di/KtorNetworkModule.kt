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
package com.keelim.core.network.di

import com.keelim.core.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Qualifier
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object KtorNetworkModule {
    @KtorWebsocketHttpClient
    @Provides
    @Singleton
    fun provideKtorWebsocketHttpClient(): HttpClient = HttpClient {
        install(WebSockets) {
            pingIntervalMillis = 20_000
        }
    }

    // add CertificatePinner
    @Provides
    @Singleton
    fun provideCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            // please add pattern and pins
            .build()
    }

    @KtorAndroidClient
    @Provides
    @Singleton
    fun providesKtorAndroidClient(
        jsonFormatter: Json,
        _certificatePinner: CertificatePinner,
        retryPolicy: RetryPolicy,
        timeoutPolicy: TimeoutPolicy,
    ): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(jsonFormatter)
            }
            install(Logging) {
                if (BuildConfig.DEBUG) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.d(message)
                        }
                    }
                } else {
                    level = LogLevel.NONE
                }
            }
            install(HttpTimeout) {
                connectTimeoutMillis = timeoutPolicy.connectTimeoutMillis
                requestTimeoutMillis = timeoutPolicy.requestTimeoutMillis
                socketTimeoutMillis = timeoutPolicy.readTimeoutMillis
            }
            install(HttpRequestRetry) {
                maxRetries = retryPolicy.maxRetries
                retryOnServerErrors(maxRetries = retryPolicy.maxRetries)
                retryIf { _, response ->
                    retryPolicy.retryOnServerErrors && response.status.isSuccess().not()
                }
                retryOnExceptionIf { _, cause ->
                    retryPolicy.shouldRetryOn(cause)
                }
                delayMillis { retry ->
                    retry * retryPolicy.baseDelayMillis
                }
            }
            install(UserAgent) {
                agent = "Ktor"
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
            install(ContentEncoding) {
                gzip()
                deflate()
            }
        }
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KtorWebsocketHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KtorAndroidClient
}
