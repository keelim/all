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
package com.keelim.data.di

import com.keelim.data.model.targetService.ServiceRetrofit
import com.keelim.data.network.CacheInterceptor
import com.keelim.data.network.TargetService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CONNECT_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 1L
    private const val READ_TIMEOUT = 20L
    private const val CNUBUS_URL = "http://service.url/"
    private const val MYGRADE_URL = "http://service.url/"
    private const val COMSSA_URL = "http://service.url/"
    private const val NANDA_URL = "http://service.url/"
    private const val YR_URL = "http://service.url/"

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cacheInterceptor: CacheInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            addInterceptor(cacheInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideCnubusRetrofit(okHttpClient: OkHttpClient): ServiceRetrofit.CnuBusRetrofit {
        return ServiceRetrofit.CnuBusRetrofit(
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(CNUBUS_URL)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideMyGradeRetrofit(okHttpClient: OkHttpClient): ServiceRetrofit.MyGradeRetrofit {
        return ServiceRetrofit.MyGradeRetrofit(
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(MYGRADE_URL)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideComssaRetrofit(okHttpClient: OkHttpClient): ServiceRetrofit.ComssaRetrofit {
        return ServiceRetrofit.ComssaRetrofit(
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(COMSSA_URL)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideNandaRetrofit(okHttpClient: OkHttpClient): ServiceRetrofit.NandaRetrofit {
        return ServiceRetrofit.NandaRetrofit(
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(NANDA_URL)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideYrRetrofit(okHttpClient: OkHttpClient): ServiceRetrofit.YrRetrofit {
        return ServiceRetrofit.YrRetrofit(
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(YR_URL)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideNandaService(serviceRetrofit: ServiceRetrofit.NandaRetrofit): TargetService.NandaService =
        serviceRetrofit.retrofit.create(TargetService.NandaService::class.java)

    @Provides
    @Singleton
    fun provideCnubusService(serviceRetrofit: ServiceRetrofit.CnuBusRetrofit): TargetService.CnubusService =
        serviceRetrofit.retrofit.create(TargetService.CnubusService::class.java)

    @Provides
    @Singleton
    fun provideComssaService(serviceRetrofit: ServiceRetrofit.ComssaRetrofit): TargetService.ComssaService =
        serviceRetrofit.retrofit.create(TargetService.ComssaService::class.java)

    @Provides
    @Singleton
    fun provideMyGradeService(serviceRetrofit: ServiceRetrofit.MyGradeRetrofit): TargetService.MyGradeService =
        serviceRetrofit.retrofit.create(TargetService.MyGradeService::class.java)

    @Provides
    @Singleton
    fun provideYrService(serviceRetrofit: ServiceRetrofit.YrRetrofit): TargetService.YrService =
        serviceRetrofit.retrofit.create(TargetService.YrService::class.java)
}
