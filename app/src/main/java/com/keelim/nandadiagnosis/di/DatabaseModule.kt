package com.keelim.nandadiagnosis.di

import android.content.Context
import androidx.room.Room
import com.keelim.nandadiagnosis.data.db.AppDatabase
import com.keelim.nandadiagnosis.data.db.DataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "nanda.db"
        ).build()
    }

    @Provides
    fun provideDao(database: AppDatabase): DataDao {
        return database.dataDao()
    }
}
