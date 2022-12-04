package com.keelim.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.keelim.data.db.dao.DataDaoV2
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.SimpleHistoryDao
import com.keelim.data.db.entity.History
import com.keelim.data.db.entity.NandaEntity
import com.keelim.data.db.entity.NandaHistory
import com.keelim.data.db.entity.SimpleHistory

@Database(
    entities = [History::class, SimpleHistory::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
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
    abstract fun dataDao(): DataDaoV2
//  abstract fun historyDao(): HistoryDao
}
