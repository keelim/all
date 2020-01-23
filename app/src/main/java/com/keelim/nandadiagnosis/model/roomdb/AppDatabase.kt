package com.keelim.nandadiagnosis.model.roomdb

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
            val DB_PATH = context.applicationInfo.dataDir + "/databases/"
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java,
                            "nanda"
                    ).createFromFile(File(DB_PATH + "nanda.db"))
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE
        }
    }
}


