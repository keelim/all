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
package com.keelim.data.di.network

import com.keelim.data.network.TargetService
import com.keelim.data.network.interceptor.CacheInterceptor
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

    @CNUBUS
    @Provides
    @Singleton
    fun provideCnubusRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(CNUBUS_URL)
            .build()
    }

    @MYGRADE
    @Provides
    @Singleton
    fun provideMyGradeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(MYGRADE_URL)
            .build()
    }

    @MYGRADE2
    @Provides
    @Singleton
    fun provideMyGrade2Retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://www.googleapis.com/")
            .build()
    }

    @COMSSA
    @Provides
    @Singleton
    fun provideComssaRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(COMSSA_URL)
            .build()
    }

    @NANDA
    @Provides
    @Singleton
    fun provideNandaRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NANDA_URL)
            .build()
    }

    @YR
    @Provides
    @Singleton
    fun provideYrRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(YR_URL)
            .build()
    }


    @Provides
    @Singleton
    fun provideNandaService(@NANDA serviceRetrofit: Retrofit): TargetService.NandaService =
        serviceRetrofit.create(TargetService.NandaService::class.java)

    @Provides
    @Singleton
    fun provideCnubusService(@CNUBUS serviceRetrofit: Retrofit): TargetService.CnubusService =
        serviceRetrofit.create(TargetService.CnubusService::class.java)

    @Provides
    @Singleton
    fun provideComssaService(@COMSSA serviceRetrofit: Retrofit): TargetService.ComssaService =
        serviceRetrofit.create(TargetService.ComssaService::class.java)

    // @Provides
    // @Singleton
    // fun provideMyGradeService(@MYGRADE serviceRetrofit: Retrofit): TargetService.MyGradeService =
    //     serviceRetrofit.create(TargetService.MyGradeService::class.java)

    @Provides
    @Singleton
    fun provideMyGrade2Service(@MYGRADE2 serviceRetrofit: Retrofit): TargetService.MyGradeService =
        serviceRetrofit.create(TargetService.MyGradeService::class.java)

    @Provides
    @Singleton
    fun provideYrService(@YR serviceRetrofit: Retrofit): TargetService.YrService =
        serviceRetrofit.create(TargetService.YrService::class.java)
}
