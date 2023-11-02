package com.keelim.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.keelim.data.UserSettings
import com.keelim.data.db.CnuAppDatabase
import com.keelim.data.db.MyGradeAppDatabase
import com.keelim.data.db.NandaAppDatabase
import com.keelim.data.model.proto.UserSettingsSerializer
import com.keelim.data.source.local.DataStoreManager
import com.keelim.data.source.local.PreferenceManager
import com.keelim.data.source.local.SharedPreferenceManager
import com.keelim.data.source.local.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
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
    fun provideCnuBusAppDatabase(
        @ApplicationContext ctx: Context,
    ): CnuAppDatabase {
        return Room.databaseBuilder(
            ctx,
            CnuAppDatabase::class.java,
            "station.db",
        ).build()
    }

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context,
    ): PreferenceManager = SharedPreferenceManager(
        context = context,
    )

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context,
    ) = TokenManager(context)

    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext ctx: Context,
        @IoDispatcher io: CoroutineDispatcher,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        migrations = listOf(SharedPreferencesMigration(ctx, "preference")),
        scope = CoroutineScope(io + SupervisorJob()),
        produceFile = { ctx.preferencesDataStoreFile("preference") },
    )

    @Provides
    @Singleton
    fun providePreferenceDataStoreManager(
        dataStore: DataStore<Preferences>,
        userSettingsDataStore: DataStore<UserSettings>,
    ): DataStoreManager = DataStoreManager(dataStore, userSettingsDataStore)

    @Provides
    @Singleton
    fun providesUserSettingsPreferencesDataStore(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @ApplicationScope coroutineScope: CoroutineScope,
        userSettingsSerializer: UserSettingsSerializer,
    ): DataStore<UserSettings> {
        return DataStoreFactory.create(
            serializer = userSettingsSerializer,
            scope = CoroutineScope(coroutineScope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("user_settings.pb")
        }
    }
}
