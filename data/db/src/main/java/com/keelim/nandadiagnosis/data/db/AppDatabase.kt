package com.keelim.nandadiagnosis.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(entities = [NandaEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "nanda")
                            .createFromFile(File(context.getExternalFilesDir(null), "nanda.db"))
//                            .createFromFile(context.getDatabasePath("nanda.db"))
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE
        }
    }
}


