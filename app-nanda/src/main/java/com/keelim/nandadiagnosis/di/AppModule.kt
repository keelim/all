package com.keelim.nandadiagnosis.di

import com.keelim.nandadiagnosis.inappupdate.InAppUpdateManager
import com.keelim.nandadiagnosis.inappupdate.InAppUpdateManagerImpl
import com.keelim.nandadiagnosis.notification.NotificationBuilder
import com.keelim.nandadiagnosis.notification.NotificationBuilderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindsNotificationBuilder(
        notificationBuilderImpl: NotificationBuilderImpl,
    ): NotificationBuilder

    @Binds
    abstract fun bindsAppUpdateManager(
        appUpdateManagerImpl: InAppUpdateManagerImpl,
    ): InAppUpdateManager
}
