

package com.keelim.data.di

import com.keelim.data.db.AppDatabase
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.SimpleHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
}
