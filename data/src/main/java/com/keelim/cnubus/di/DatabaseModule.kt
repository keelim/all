/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.di

import android.app.Activity
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.keelim.cnubus.data.db.AppDatabase
import com.keelim.cnubus.data.db.DataStoreManager
import com.keelim.cnubus.data.db.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DATABASE_NAME = "station.db"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext ctx: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            ctx,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext ctx: Context
    ): SharedPreferenceManager {
        return SharedPreferenceManager(ctx.getSharedPreferences("preference", Activity.MODE_PRIVATE))
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
}
