package com.keelim.mygrade.di

import com.keelim.mygrade.notification.NotificationBuilder
import com.keelim.mygrade.notification.NotificationBuilderImpl
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
}
