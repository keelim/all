package com.keelim.data.di

import com.keelim.data.db.MyGradeAppDatabase
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.SimpleHistoryDao
import com.keelim.data.network.TargetService
import com.keelim.data.repository.RemoteDataSource
import com.keelim.data.repository.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providesAuthorDao(
        database: MyGradeAppDatabase,
    ): HistoryDao = database.historyDao()

    @Provides
    fun providesTopicsDao(
        database: MyGradeAppDatabase,
    ): SimpleHistoryDao = database.simpleHistoryDao()

    @Provides
    @Singleton
    fun provideRemoteDataSource(nandaService: TargetService.NandaService): RemoteDataSource {
        return RemoteDataSourceImpl(nandaService)
    }
}
