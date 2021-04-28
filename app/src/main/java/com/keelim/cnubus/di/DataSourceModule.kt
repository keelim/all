package com.keelim.cnubus.di

import com.keelim.cnubus.data.remote.RemoteDataSource
import com.keelim.cnubus.data.remote.RemoteDataSourceImpl
import com.keelim.cnubus.services.RoadService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped


@Module
@InstallIn(ActivityRetainedComponent::class)
object DataSourceModule {
    @Provides
    @ActivityRetainedScoped
    fun provideRemoteDataSource(roadService: RoadService): RemoteDataSource{
        return RemoteDataSourceImpl(roadService)
    }
}