package com.keelim.cnubus.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun LocationDao(): LocationDao

    companion object {
        private var INSTANCE: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase? {
            val DB_PATH = context.applicationInfo.dataDir + "/databases/"
            if (INSTANCE == null) {
                synchronized(LocationDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocationDatabase::class.java,
                        "location"
                    ).createFromFile(File(DB_PATH, "location.db"))
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}