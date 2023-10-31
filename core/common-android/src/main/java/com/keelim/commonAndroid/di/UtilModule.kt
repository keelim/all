package com.keelim.commonAndroid.di

import com.keelim.common.model.NetworkConnectivityService
import com.keelim.commonAndroid.core.ErrorDelegate
import com.keelim.commonAndroid.core.ErrorDelegateImpl
import com.keelim.commonAndroid.util.NetworkConnectivityServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UtilModule {
    @Binds
    fun bindsNetworkConnectivityService(
        repository: NetworkConnectivityServiceImpl,
    ): NetworkConnectivityService

    @Binds
    fun bindsErrorDelegate(
        delegateImpl: ErrorDelegateImpl
    ): ErrorDelegate
}
