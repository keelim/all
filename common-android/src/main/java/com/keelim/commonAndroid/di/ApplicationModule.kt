package com.keelim.commonAndroid.di

import android.content.Context
import com.keelim.commonAndroid.model.AppInfo
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {
    @Singleton
    fun provideAppInfo(
        @ApplicationContext context: Context
    ): AppInfo = AppInfo(context.packageName)
}
