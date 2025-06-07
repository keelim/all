package com.keelim.shared.data.database

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.keelim.shared.data.database.dao.AlarmDao
import com.keelim.shared.data.database.dao.ArduconDao
import com.keelim.shared.data.database.dao.HistoryDao
import com.keelim.shared.data.database.dao.NandaDao
import com.keelim.shared.data.database.dao.NetworkCacheDao
import com.keelim.shared.data.database.dao.NoteDao
import com.keelim.shared.data.database.dao.TaskDao
import com.keelim.shared.data.database.dao.TimerHistoryDao
import com.keelim.shared.data.database.model.AlarmEntity
import com.keelim.shared.data.database.model.DeepLinkEntity
import com.keelim.shared.data.database.model.History
import com.keelim.shared.data.database.model.LocalTask
import com.keelim.shared.data.database.model.NandaEntity
import com.keelim.shared.data.database.model.NetworkCache
import com.keelim.shared.data.database.model.NoticesEntity
import com.keelim.shared.data.database.model.SchemeEntity
import com.keelim.shared.data.database.model.SimpleHistory
import com.keelim.shared.data.database.model.TimerHistory

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MyGradeAppDatabaseConstructor : RoomDatabaseConstructor<MyGradeAppDatabase> {
    override fun initialize(): MyGradeAppDatabase
}


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
@ConstructedBy(MyGradeAppDatabaseConstructor::class)
abstract class MyGradeAppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun taskDao(): TaskDao
    abstract fun timerHistoryDao(): TimerHistoryDao
    abstract fun networkCacheDao(): NetworkCacheDao
    abstract fun noteDao(): NoteDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object NandaAppDatabaseConstructor : RoomDatabaseConstructor<NandaAppDatabase> {
    override fun initialize(): NandaAppDatabase
}

@Database(
    entities = [NandaEntity::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(NandaAppDatabaseConstructor::class)
abstract class NandaAppDatabase : RoomDatabase() {
    abstract fun dataDao(): NandaDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ArduconDatabaseConstructor : RoomDatabaseConstructor<ArduconDatabase> {
    override fun initialize(): ArduconDatabase
}
@Database(
    entities = [
        DeepLinkEntity::class,
        SchemeEntity::class,
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
    ],
)
@ConstructedBy(ArduconDatabaseConstructor::class)
abstract class ArduconDatabase : RoomDatabase() {
    abstract fun dataDao(): ArduconDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AllDatabaseConstructor : RoomDatabaseConstructor<AllDatabase> {
    override fun initialize(): AllDatabase
}
@Database(
    entities = [
        AlarmEntity::class,
    ],
    version = 1,
    exportSchema = true,
    // autoMigrations = []
)
@ConstructedBy(AllDatabaseConstructor::class)
abstract class AllDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}
