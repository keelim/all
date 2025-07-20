package com.keelim.core.database

import android.content.Context
import com.keelim.shared.data.database.AllDatabase
import com.keelim.shared.data.database.ArduconDatabase
import com.keelim.shared.data.database.MyGradeAppDatabase
import com.keelim.shared.data.database.NandaAppDatabase
import com.keelim.shared.data.database.createDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): MyGradeAppDatabase = createDatabase<MyGradeAppDatabase>(
        context,
        "mygrade",
    )

    @Provides
    @Singleton
    fun provideArduconDatabase(
        @ApplicationContext context: Context,
    ): ArduconDatabase = createDatabase<ArduconDatabase>(
        context,
        "arducon.db",
    )

    @Provides
    @Singleton
    fun provideAllDatabase(
        @ApplicationContext context: Context,
    ): AllDatabase = createDatabase<AllDatabase>(
        context,
        "all.db",
    )

    @Provides
    @Singleton
    fun provideNandaAppDatabase(
        @ApplicationContext context: Context,
    ): NandaAppDatabase = createDatabase<NandaAppDatabase>(
        context,
        "nanda.db",
    )
}
