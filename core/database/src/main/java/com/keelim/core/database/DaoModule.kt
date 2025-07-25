package com.keelim.core.database

import com.keelim.shared.data.database.AllDatabase
import com.keelim.shared.data.database.ArduconDatabase
import com.keelim.shared.data.database.MyGradeAppDatabase
import com.keelim.shared.data.database.NandaAppDatabase
import com.keelim.shared.data.database.dao.AlarmDao
import com.keelim.shared.data.database.dao.ArduconDao
import com.keelim.shared.data.database.dao.HistoryDao
import com.keelim.shared.data.database.dao.NetworkCacheDao
import com.keelim.shared.data.database.dao.NoteDao
import com.keelim.shared.data.database.dao.TaskDao
import com.keelim.shared.data.database.dao.TimerHistoryDao
import com.keelim.shared.data.database.dao.LengthRecordDao
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

    @Provides
    @Singleton
    fun providesArduconDao(
        database: ArduconDatabase,
    ): ArduconDao = database.dataDao()

    @Provides
    @Singleton
    fun providesAlarmDao(
        database: AllDatabase,
    ): AlarmDao = database.alarmDao()

    @Provides
    @Singleton
    fun providesLengthRecordDao(
        database: NandaAppDatabase,
    ): LengthRecordDao = database.lengthRecordDao()
}
