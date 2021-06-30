package com.keelim.comssa.di

import com.keelim.comssa.data.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideDataApi(): DataApi {
        return DataApiImpl()
    }

    @Provides
    @Singleton
    fun provideReviewApi(): ReviewApi {
        return ReviewApiImpl()
    }

    @Provides
    @Singleton
    fun provideUserApi(): UserApi{
        return UserApiImpl()
    }
}