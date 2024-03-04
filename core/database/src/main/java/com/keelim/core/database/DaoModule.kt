package com.keelim.core.database

import com.keelim.core.database.dao.CnuHistoryDao
import com.keelim.core.database.dao.CommentDao
import com.keelim.core.database.dao.HistoryDao
import com.keelim.core.database.dao.NandaDao
import com.keelim.core.database.dao.NetworkCacheDao
import com.keelim.core.database.dao.NoteDao
import com.keelim.core.database.dao.TaskDao
import com.keelim.core.database.dao.TimerHistoryDao
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
    fun providesTaskDao(
        database: MyGradeAppDatabase,
    ): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun providesNetworkCache(
        database: MyGradeAppDatabase,
    ): NetworkCacheDao = database.networkCacheDao()

    @Provides
    @Singleton
    fun provideNoteDao(
        database: MyGradeAppDatabase,
    ): NoteDao = database.noteDao()
}
