package com.keelim.comssa.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.keelim.comssa.data.db.dao.SearchDao
import com.keelim.comssa.data.db.entity.Search
import java.io.File

@Database(entities = [Search::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract val searchDao:SearchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "comssa"
                    )
                        .createFromFile(File(context.getExternalFilesDir(null), "comssa.db"))
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}