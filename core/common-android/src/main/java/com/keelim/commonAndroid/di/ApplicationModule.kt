package com.keelim.commonAndroid.di

import android.content.Context
import com.keelim.commonAndroid.model.AppInfo
import com.keelim.commonAndroid.util.ApplicationMonitor
import com.keelim.commonAndroid.util.DownloadReceiver
import com.keelim.shared.data.UserStateStore
import com.keelim.shared.di.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
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

    @Provides
    @Singleton
    fun providesDownloadReceiver(
        @ApplicationContext context: Context,
    ): DownloadReceiver = DownloadReceiver(
        context,
    )

    @Provides
    @Singleton
    fun providesUserStateStore(
        @ApplicationContext context: Context,
    ): UserStateStore = Module(
        context = context,
    ).createUserStateStore()
}
