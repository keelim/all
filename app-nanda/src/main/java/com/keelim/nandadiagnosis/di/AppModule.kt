package com.keelim.nandadiagnosis.di

import com.keelim.nandadiagnosis.notification.NotificationBuilder
import com.keelim.nandadiagnosis.notification.NotificationBuilderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Binds
    fun bindsNotificationBuilder(
        notificationBuilderImpl: NotificationBuilderImpl,
    ): NotificationBuilder
}
