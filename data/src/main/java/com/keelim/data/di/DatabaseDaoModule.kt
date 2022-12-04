package com.keelim.data.di

import android.content.Context
import androidx.room.Room
import com.keelim.data.db.AppDatabase
import com.keelim.data.db.NandaAppDatabase
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.SimpleHistoryDao
import com.keelim.data.network.NandaService
import com.keelim.data.repository.RemoteDataSource
import com.keelim.data.repository.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseDaoModule {
    @Provides
    fun providesAuthorDao(
        database: AppDatabase,
    ): HistoryDao = database.historyDao()

    @Provides
    fun providesTopicsDao(
        database: AppDatabase,
    ): SimpleHistoryDao = database.simpleHistoryDao()

    @Provides
    @Singleton
    fun provideRemoteDataSource(nandaService: NandaService): RemoteDataSource {
        return RemoteDataSourceImpl(nandaService)
    }
}
