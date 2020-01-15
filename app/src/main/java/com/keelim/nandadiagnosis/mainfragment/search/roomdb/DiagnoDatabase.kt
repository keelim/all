package com.keelim.nandadiagnosis.mainfragment.search.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Diagnosis::class), version = 1, exportSchema = true)
abstract class DiagnoDatabase : RoomDatabase() {

    abstract fun getDiagnosisDao(): DiagnosisDao

    companion object {
        private var INSTANCE: DiagnoDatabase? = null

        fun getInstance(context: Context): DiagnoDatabase? {
            if (INSTANCE == null) {
                synchronized(DiagnoDatabase::class) {
                    INSTANCE == Room.databaseBuilder(
                            context,
                            DiagnoDatabase::class.java,
                            "nanda.db"
                    ).build()
                }
            }
            return INSTANCE
        }

    }
}