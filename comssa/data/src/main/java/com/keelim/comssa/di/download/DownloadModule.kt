package com.keelim.comssa.di.download

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DownloadModule {
    @Provides
    @Singleton
    fun provideDownloadReceiver(
        @ApplicationContext ctx: Context
    ): DownloadReceiver {
        return DownloadReceiver(ctx)
    }

    @Provides
    @Singleton
    fun provideDownloadRequest(
        @ApplicationContext ctx: Context
    ): DownloadRequest {
        return DownloadRequest(ctx)
    }
}
