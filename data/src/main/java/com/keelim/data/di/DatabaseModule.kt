package com.keelim.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.keelim.data.db.CnuAppDatabase
import com.keelim.data.source.local.DataStoreManager
import com.keelim.data.db.MyGradeAppDatabase
import com.keelim.data.db.NandaAppDatabase
import com.keelim.data.source.local.PreferenceManager
import com.keelim.data.source.local.SharedPreferenceManager
import com.keelim.data.source.local.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext ctx: Context,
    ): MyGradeAppDatabase = Room.databaseBuilder(
        ctx, MyGradeAppDatabase::class.java, "mygrade"
    ).build()

    @Provides
    @Singleton
    fun provideNandaAppDatabase(
        @ApplicationContext context: Context,
    ): NandaAppDatabase {
        return Room.databaseBuilder(
            context, NandaAppDatabase::class.java, "nanda"
        ).createFromFile(File(context.getExternalFilesDir(null), "nanda.db"))
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideCnuBusAppDatabase(
        @ApplicationContext ctx: Context
    ): CnuAppDatabase {
        return Room.databaseBuilder(
            ctx,
            CnuAppDatabase::class.java,
            "station.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext ctx: Context,
        @IoDispatcher io: CoroutineDispatcher
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        migrations = listOf(SharedPreferencesMigration(ctx, "preference")),
        scope = CoroutineScope(io + SupervisorJob()),
        produceFile = { ctx.preferencesDataStoreFile("preference") }
    )

    @Provides
    @Singleton
    fun providePreferenceDataStoreManager(
        dataStore: DataStore<Preferences>
    ): DataStoreManager = DataStoreManager(dataStore)

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context
    ): PreferenceManager = SharedPreferenceManager(
        context = context
    )

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context
    ) = TokenManager(context)
}
