package com.keelim.comssa.di

import com.keelim.comssa.data.repository.DataRepository
import com.keelim.comssa.data.repository.IoRepository
import com.keelim.comssa.data.repository.ReviewRepository
import com.keelim.comssa.usecase.*
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

    @Provides
    @Singleton
    fun provideGetAkkDatasUseCase(
        dataRepository: DataRepository,
    ): GetAllDatasUseCase{
        return GetAllDatasUseCase(
            dataRepository = dataRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetRandomFeatureDataUseCase(
        dataRepository: DataRepository,
        reviewRepository: ReviewRepository,
    ): GetRandomFeatureDataUseCase{
        return GetRandomFeatureDataUseCase(
            dataRepository = dataRepository,
            reviewRepository = reviewRepository
        )
    }

}