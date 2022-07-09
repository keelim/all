package com.keelim.data.di

import android.content.Context
import androidx.room.Room
import com.keelim.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext ctx: Context
    ): AppDatabase = Room.databaseBuilder(
        ctx,
        AppDatabase::class.java,
        "mygrade"
    ).build()
}
