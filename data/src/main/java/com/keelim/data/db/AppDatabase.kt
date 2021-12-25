package com.keelim.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.entity.History

@Database(
    entities = [History::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase()  {
    abstract fun historyDao(): HistoryDao
}