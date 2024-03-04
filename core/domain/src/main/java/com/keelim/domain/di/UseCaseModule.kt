package com.keelim.domain.di

import com.keelim.core.data.source.local.DataStoreManager
import com.keelim.domain.ShowEventUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun providesShowEventUseCase(
        dataStoreManager: DataStoreManager,
    ): ShowEventUseCase = ShowEventUseCase(dataStoreManager)
}
