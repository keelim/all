package com.keelim.nandadiagnosis.mainfragment.search.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = arrayOf(Diagnosis::class), version = 2)
abstract class DiagnoDatabase : RoomDatabase() {

    abstract fun getDiagnosisDao(): DiagnosisDao


    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        private var INSTANCE: DiagnoDatabase? = null

        fun getInstance(context: Context): DiagnoDatabase? {
            if (INSTANCE == null) {
                synchronized(DiagnoDatabase::class) {
                    INSTANCE == Room.databaseBuilder(
                            context.applicationContext,
                            DiagnoDatabase::class.java,
                            "nanda.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}