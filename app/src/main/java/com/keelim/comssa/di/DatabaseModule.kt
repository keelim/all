package com.keelim.comssa.di

import android.content.Context
import com.keelim.comssa.data.db.AppDatabase
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
    fun provideAppDataBase(
        @ApplicationContext context:Context
    ):AppDatabase {
        return AppDatabase.getInstance(context)
    }
}