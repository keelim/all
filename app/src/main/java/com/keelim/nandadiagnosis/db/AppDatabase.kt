package com.keelim.nandadiagnosis.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(entities = [NandaEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao

    companion object {
        private var INSTANCE: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase? {
            val dbPath = context.applicationInfo.dataDir + "/databases/"
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                                    AppDatabase::class.java,
                                    "nanda"
                            ).createFromFile(File(dbPath + "nanda.db"))
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE
        }
    }
}


