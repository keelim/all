package com.keelim.commonAndroid.di

import com.keelim.common.model.NetworkConnectivityService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {
    @Binds
    abstract fun bindsNetworkConnectivityService(
        repository: NetworkConnectivityServiceImpl,
    ): NetworkConnectivityService
}
