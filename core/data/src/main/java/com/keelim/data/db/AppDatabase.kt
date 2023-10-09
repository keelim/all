package com.keelim.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.keelim.data.db.dao.CnuHistoryDao
import com.keelim.data.db.dao.CommentDao
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.NandaDao
import com.keelim.data.db.dao.StationDao
import com.keelim.data.db.dao.TaskDao
import com.keelim.data.db.dao.TimerHistoryDao
import com.keelim.data.model.entity.CnuHistory
import com.keelim.data.model.entity.Comment
import com.keelim.data.model.entity.NandaEntity
import com.keelim.data.model.entity.StationEntity
import com.keelim.data.model.entity.StationSubwayCrossRefEntity
import com.keelim.data.model.entity.SubwayEntity
import com.keelim.data.source.local.History
import com.keelim.data.source.local.LocalTask
import com.keelim.data.source.local.SimpleHistory
import com.keelim.data.source.local.TimerHistory

@Database(
    entities = [History::class, LocalTask::class, SimpleHistory::class, TimerHistory::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 2, to = 3)
    ]
)
abstract class MyGradeAppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun taskDao(): TaskDao

    abstract fun timerHistoryDao(): TimerHistoryDao
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
        StationEntity::class,
        SubwayEntity::class,
        StationSubwayCrossRefEntity::class,
        Comment::class,
        CnuHistory::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class CnuAppDatabase : RoomDatabase() {
    abstract fun daoStation(): StationDao
    abstract fun daoComment(): CommentDao
    abstract fun daoHistory(): CnuHistoryDao
}
