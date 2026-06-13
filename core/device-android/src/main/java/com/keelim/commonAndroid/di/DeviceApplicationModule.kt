package com.keelim.commonAndroid.di

import android.content.Context
import com.keelim.commonAndroid.model.AppInfo
import com.keelim.commonAndroid.util.ApplicationMonitor
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
internal object DeviceApplicationModule {
    @Provides
    @Singleton
    fun providesAppInfo(
        @ApplicationContext context: Context,
    ): AppInfo = AppInfo(
        context.packageName,
        adId = "",
    )

    @Provides
    @Singleton
    fun providesApplicationMonitor(
        appInfo: AppInfo,
    ): ApplicationMonitor = ApplicationMonitor(
        appInfo,
    )
}
