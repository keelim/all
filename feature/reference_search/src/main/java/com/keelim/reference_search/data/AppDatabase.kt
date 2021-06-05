package com.keelim.reference_search.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [History::class], version=1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}