package com.keelim.core.data.di

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.keelim.core.data.BuildConfig
import com.keelim.core.data.source.local.ArduconDataSourceImpl
import com.keelim.core.data.source.local.SharedPreferenceManager
import com.keelim.core.data.source.local.TokenManager
import com.keelim.core.database.dao.ArduconDao
import com.keelim.core.database.repository.ArduconDataSource
import com.keelim.data.repository.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context,
    ): PreferenceManager = SharedPreferenceManager(
        context = context,
    )

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context,
    ) = TokenManager(context)

    // Local
    @Singleton
    @Provides
    fun provideArduconDataSource(
        dao: ArduconDao,
    ): ArduconDataSource = ArduconDataSourceImpl(dao)

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GENERATIVE,
    )
}
