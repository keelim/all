package com.keelim.scheme.di

import android.content.Context
import com.keelim.scheme.notification.SchemeNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun providesSchemeNotificationManager(
        @ApplicationContext context: Context,
    ): SchemeNotificationManager = SchemeNotificationManager(
        context
    )
}
