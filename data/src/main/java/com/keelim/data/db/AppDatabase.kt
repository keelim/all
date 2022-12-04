package com.keelim.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.keelim.data.db.dao.CnuHistoryDao
import com.keelim.data.db.dao.CommentDao
import com.keelim.data.db.dao.NandaDao
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.SimpleHistoryDao
import com.keelim.data.db.dao.StationDao
import com.keelim.data.db.entity.CnuHistory
import com.keelim.data.db.entity.Comment
import com.keelim.data.db.entity.History
import com.keelim.data.db.entity.NandaEntity
import com.keelim.data.db.entity.NandaHistory
import com.keelim.data.db.entity.SimpleHistory
import com.keelim.data.db.entity.StationEntity
import com.keelim.data.db.entity.StationSubwayCrossRefEntity
import com.keelim.data.db.entity.SubwayEntity

@Database(
    entities = [History::class, SimpleHistory::class],
    version = 1,
    exportSchema = false
)
abstract class MyGradeAppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun simpleHistoryDao(): SimpleHistoryDao
}

@Database(
    entities = [NandaEntity::class, NandaHistory::class],
//  autoMigrations = [
//    AutoMigration(from = 1, to = 2),
//    AutoMigration(from = 2, to = 3),
//  ],
    version = 1,
    exportSchema = true,
)

abstract class NandaAppDatabase : RoomDatabase() {
    abstract fun dataDao(): NandaDao
//  abstract fun historyDao(): HistoryDao
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
