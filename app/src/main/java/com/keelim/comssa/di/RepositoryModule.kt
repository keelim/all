package com.keelim.comssa.di

import com.keelim.comssa.data.api.DataApi
import com.keelim.comssa.data.api.ReviewApi
import com.keelim.comssa.data.db.AppDatabase
import com.keelim.comssa.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideIoRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        db: AppDatabase,
    ): IoRepository {
        return IoRepositoryImpl(
            ioDispatcher = ioDispatcher,
            db = db
        )
    }

    @Provides
    @Singleton
    fun provideDataRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        dataApi: DataApi,
    ): DataRepository {
        return DataRepositoryImpl(
            dataApi = dataApi,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideReviewRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        reviewApi: ReviewApi,
    ): ReviewRepository {
        return ReviewRepositoryImpl(
            reviewApi = reviewApi,
            ioDispatcher = ioDispatcher
        )
    }
}