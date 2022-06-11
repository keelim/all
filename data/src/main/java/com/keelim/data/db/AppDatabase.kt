package com.keelim.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.SimpleHistoryDao
import com.keelim.data.db.entity.History
import com.keelim.data.db.entity.SimpleHistory

@Database(
    entities = [History::class, SimpleHistory::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase()  {
    abstract fun historyDao(): HistoryDao
    abstract fun simpleHistoryDao(): SimpleHistoryDao
}
