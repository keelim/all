package com.keelim.cnubus.di

import com.keelim.cnubus.data.remote.RemoteDataSource
import com.keelim.cnubus.data.repository.Repository
import com.keelim.cnubus.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun providerRepository(remoteDataSource: RemoteDataSource): Repository {
        return RepositoryImpl(remoteDataSource)
    }
}