/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.keelim.core.network.BuildConfig
import com.keelim.core.network.interceptor.CacheInterceptor
import com.keelim.core.network.interceptor.RetryingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetryPolicy(): RetryPolicy {
        return RetryPolicy(
            maxRetries = 3,
            baseDelayMillis = 2_000L,
            retryOnServerErrors = true,
            retryOnConnectionErrors = true,
        )
    }

    @Provides
    @Singleton
    fun provideTimeoutPolicy(): TimeoutPolicy {
        return TimeoutPolicy(
            connectTimeoutMillis = 10_000L,
            writeTimeoutMillis = 1_000L,
            readTimeoutMillis = 20_000L,
            requestTimeoutMillis = 30_000L,
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cacheInterceptor: CacheInterceptor,
        retryingInterceptor: RetryingInterceptor,
        timeoutPolicy: TimeoutPolicy,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(timeoutPolicy.connectTimeoutMillis, TimeUnit.MILLISECONDS)
            writeTimeout(timeoutPolicy.writeTimeoutMillis, TimeUnit.MILLISECONDS)
            readTimeout(timeoutPolicy.readTimeoutMillis, TimeUnit.MILLISECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(
                        if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BODY
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        },
                    )
                },
            )
            addInterceptor(cacheInterceptor)
            addInterceptor(retryingInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideOkHttpCallFactory(okHttpClient: OkHttpClient): Call.Factory = okHttpClient

    @Provides
    @Singleton
    fun provideImageLoader(
        // We specifically request dagger.Lazy here, so that it's not instantiated from Dagger.
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
        @ApplicationContext application: Context,
    ): ImageLoader =
        ImageLoader.Builder(application)
            .callFactory { okHttpCallFactory.get() }
            .components { add(SvgDecoder.Factory()) }
            // Assume most content images are versioned urls
            // but some problematic images are fetching each time
            .respectCacheHeaders(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
        }
    }
}
