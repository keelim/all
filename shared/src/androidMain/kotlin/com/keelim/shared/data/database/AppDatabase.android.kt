package com.keelim.shared.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

inline fun <reified T : RoomDatabase> createDatabase(
    context: Context,
    name: String,
    allowExternalDb: String = "",
): T {
    val builder = Room.databaseBuilder(
        context,
        T::class.java,
        name,
    ).fallbackToDestructiveMigration(false)
        .setDriver(BundledSQLiteDriver())

    if (allowExternalDb.isNotEmpty()) {
        builder.createFromFile(File(context.getExternalFilesDir(null), allowExternalDb))
    }

    return builder.build()
}
