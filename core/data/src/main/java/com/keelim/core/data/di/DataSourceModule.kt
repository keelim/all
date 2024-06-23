package com.keelim.core.data.di

import android.content.Context
import com.keelim.core.data.source.local.ArduconDataSource
import com.keelim.core.data.source.local.ArduconDataSourceImpl
import com.keelim.core.data.source.local.PreferenceManager
import com.keelim.core.data.source.local.SharedPreferenceManager
import com.keelim.core.data.source.local.TokenManager
import com.keelim.core.database.dao.ArduconDao
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
        dao: ArduconDao
    ): ArduconDataSource = ArduconDataSourceImpl(dao)
}
