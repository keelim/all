package com.keelim.commonAndroid.di

import android.content.Context
import com.keelim.commonAndroid.util.DownloadReceiver
import com.keelim.shared.data.UserStateStore
import com.keelim.shared.di.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
internal object ApplicationModule {
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
