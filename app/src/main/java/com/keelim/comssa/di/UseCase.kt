package com.keelim.comssa.di

import com.keelim.comssa.data.repository.IoRepository
import com.keelim.comssa.usecase.GetFavoriteUseCase
import com.keelim.comssa.usecase.SearchUseCase
import com.keelim.comssa.usecase.UpdateFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCase {
    @Provides
    @Singleton
    fun provideSearchUseCase(
        ioRepository: IoRepository,
    ): SearchUseCase{
        return SearchUseCase(
            ioRepository = ioRepository
        )
    }

    @Provides
    @Singleton
    fun provideUpdateFavorite(
        ioRepository: IoRepository,
    ): UpdateFavoriteUseCase {
        return UpdateFavoriteUseCase(
            ioRepository = ioRepository
        )
    }

    @Provides
    @Singleton
    fun getFavorite(
        ioRepository: IoRepository,
    ): GetFavoriteUseCase{
        return GetFavoriteUseCase(
            ioRepository = ioRepository
        )
    }
}