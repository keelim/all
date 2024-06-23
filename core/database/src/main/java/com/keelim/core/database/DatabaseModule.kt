package com.keelim.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext ctx: Context,
    ): MyGradeAppDatabase = Room.databaseBuilder(
        ctx,
        MyGradeAppDatabase::class.java,
        "mygrade",
    ).build()

    @Provides
    @Singleton
    fun provideNandaAppDatabase(
        @ApplicationContext context: Context,
    ): NandaAppDatabase {
        return Room.databaseBuilder(
            context,
            NandaAppDatabase::class.java,
            "nanda",
        ).createFromFile(File(context.getExternalFilesDir(null), "nanda.db"))
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideArduconDatabase(
        @ApplicationContext context: Context,
    ): ArduconDatabase {
        return Room.databaseBuilder(
            context,
            ArduconDatabase::class.java,
            "arducon.db",
        ).build()
    }
}
