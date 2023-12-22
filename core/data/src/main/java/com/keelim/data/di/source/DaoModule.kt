package com.keelim.data.di.source

import com.keelim.data.db.CnuAppDatabase
import com.keelim.data.db.MyGradeAppDatabase
import com.keelim.data.db.NandaAppDatabase
import com.keelim.data.db.dao.CnuHistoryDao
import com.keelim.data.db.dao.CommentDao
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.NandaDao
import com.keelim.data.db.dao.NetworkCacheDao
import com.keelim.data.db.dao.StationDao
import com.keelim.data.db.dao.TaskDao
import com.keelim.data.db.dao.TimerHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun providesCnuHistoryDao(
        database: CnuAppDatabase,
    ): CnuHistoryDao = database.daoHistory()

    @Provides
    @Singleton
    fun providesCommentDao(
        database: CnuAppDatabase,
    ): CommentDao = database.daoComment()

    @Provides
    @Singleton
    fun providesHistoryDao(
        database: MyGradeAppDatabase,
    ): HistoryDao = database.historyDao()

    @Provides
    @Singleton
    fun providesTimerHistoryDao(
        database: MyGradeAppDatabase,
    ): TimerHistoryDao = database.timerHistoryDao()

    @Provides
    @Singleton
    fun providesNandaDao(
        database: NandaAppDatabase,
    ): NandaDao = database.dataDao()

    @Provides
    @Singleton
    fun providesStationDao(
        database: CnuAppDatabase,
    ): StationDao = database.daoStation()

    @Provides
    @Singleton
    fun providesTaskDao(
        database: MyGradeAppDatabase,
    ): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun providesNetworkCache(
        database: MyGradeAppDatabase,
    ): NetworkCacheDao = database.networkCacheDao()
}
