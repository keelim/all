package com.keelim.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.keelim.core.database.dao.AlarmDao
import com.keelim.core.database.dao.ArduconDao
import com.keelim.core.database.dao.HistoryDao
import com.keelim.core.database.dao.NandaDao
import com.keelim.core.database.dao.NetworkCacheDao
import com.keelim.core.database.dao.NoteDao
import com.keelim.core.database.dao.TaskDao
import com.keelim.core.database.dao.TimerHistoryDao
import com.keelim.core.database.model.AlarmEntity
import com.keelim.core.database.model.DeepLinkEntity
import com.keelim.core.database.model.History
import com.keelim.core.database.model.LocalTask
import com.keelim.core.database.model.NandaEntity
import com.keelim.core.database.model.NetworkCache
import com.keelim.core.database.model.NoticesEntity
import com.keelim.core.database.model.SchemeEntity
import com.keelim.core.database.model.SimpleHistory
import com.keelim.core.database.model.TimerHistory


@Database(
    entities = [
        History::class,
        LocalTask::class,
        SimpleHistory::class,
        TimerHistory::class,
        NetworkCache::class,
        NoticesEntity::class,
    ],
    version = 6,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
    ],
)
abstract class MyGradeAppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun taskDao(): TaskDao
    abstract fun timerHistoryDao(): TimerHistoryDao
    abstract fun networkCacheDao(): NetworkCacheDao
    abstract fun noteDao(): NoteDao
}

@Database(
    entities = [NandaEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class NandaAppDatabase : RoomDatabase() {
    abstract fun dataDao(): NandaDao
}

@Database(
    entities = [
        DeepLinkEntity::class,
        SchemeEntity::class,
    ],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
    ],
)
abstract class ArduconDatabase : RoomDatabase() {
    abstract fun dataDao(): ArduconDao
}

@Database(
    entities = [
        AlarmEntity::class,
    ],
    version = 1,
    exportSchema = true,
    // autoMigrations = []
)
abstract class AllDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}
