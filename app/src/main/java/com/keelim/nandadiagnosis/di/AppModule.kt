package com.keelim.nandadiagnosis.di

import com.google.android.play.core.appupdate.AppUpdateManager
import com.keelim.nandadiagnosis.inappupdate.InAppUpdateManagerImpl
import com.keelim.nandadiagnosis.notification.NotificationBuilder
import com.keelim.nandadiagnosis.notification.NotificationBuilderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindsNotificationBuilder(
        notificationBuilderImpl: NotificationBuilderImpl
    ): NotificationBuilder

    @Binds
    abstract fun bindsAppUpdateManager(
        appUpdateManagerImpl: InAppUpdateManagerImpl
    ): AppUpdateManager
}
