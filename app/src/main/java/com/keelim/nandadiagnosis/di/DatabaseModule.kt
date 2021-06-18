package com.keelim.nandadiagnosis.di

import android.content.Context
import com.keelim.nandadiagnosis.data.db.AppDatabaseV2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabaseV2 {
        return AppDatabaseV2.getInstance(context)!!
    }
}