package com.keelim.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.keelim.core.database.dao.CnuHistoryDao
import com.keelim.core.database.dao.CommentDao
import com.keelim.core.database.dao.HistoryDao
import com.keelim.core.database.dao.NandaDao
import com.keelim.core.database.dao.NetworkCacheDao
import com.keelim.core.database.dao.NoteDao
import com.keelim.core.database.dao.TaskDao
import com.keelim.core.database.dao.TimerHistoryDao
import com.keelim.core.database.model.CnuHistory
import com.keelim.core.database.model.Comment
import com.keelim.core.database.model.History
import com.keelim.core.database.model.LocalTask
import com.keelim.core.database.model.NandaEntity
import com.keelim.core.database.model.NetworkCache
import com.keelim.core.database.model.Notices
import com.keelim.core.database.model.SimpleHistory
import com.keelim.core.database.model.TimerHistory
import java.util.Date

internal class MyGradeTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let(::Date)

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
@Database(
    entities = [
        History::class,
        LocalTask::class,
        SimpleHistory::class,
        TimerHistory::class,
        NetworkCache::class,
        Notices::class,
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
@TypeConverters(MyGradeTypeConverters::class)
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


@DeleteTable(tableName = "StationEntity")
@DeleteTable(tableName = "SubwayEntity")
@DeleteTable(tableName = "StationSubwayCrossRefEntity")
class CnuAppDatabaseVersion2To3 : AutoMigrationSpec

@Database(
    entities = [
        Comment::class,
        CnuHistory::class,
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3, spec = CnuAppDatabaseVersion2To3::class),
    ],
)
abstract class CnuAppDatabase : RoomDatabase() {
    abstract fun daoComment(): CommentDao
    abstract fun daoHistory(): CnuHistoryDao
}
