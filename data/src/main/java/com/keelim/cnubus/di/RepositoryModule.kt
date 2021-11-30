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

import android.content.Context
import com.keelim.cnubus.data.api.StationArrivalsApi
import com.keelim.cnubus.data.db.AppDatabase
import com.keelim.cnubus.data.db.SharedPreferenceManager
import com.keelim.cnubus.data.repository.setting.DeveloperRepository
import com.keelim.cnubus.data.repository.setting.DeveloperRepositoryImpl
import com.keelim.cnubus.data.repository.station.StationApi
import com.keelim.cnubus.data.repository.station.StationApiImpl
import com.keelim.cnubus.data.repository.station.StationRepository
import com.keelim.cnubus.data.repository.station.StationRepositoryImpl
import com.keelim.cnubus.data.repository.theme.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(SingletonComponent::class)
@Module(includes = [RepositoryModule.ThemeModule::class])
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindsDeveloperRepository(
        repository: DeveloperRepositoryImpl,
    ): DeveloperRepository

    @Binds
    abstract fun bindsStationApiRepository(
        api: StationApiImpl,
    ): StationApi

    @InstallIn(SingletonComponent::class)
    @Module
    internal object ThemeModule {
        @Provides
        @Singleton
        fun provideThemeRepository(
            @ApplicationContext context: Context,
        ): ThemeRepository {
            return ThemeRepository(
                context
            )
        }
    }

    @InstallIn(SingletonComponent::class)
    @Module
    internal object StationModule {
        @Provides
        @Singleton
        fun provideStationRepository(
            stationArrivalsApi: StationArrivalsApi,
            stationApi: StationApi,
            appDatabase: AppDatabase,
            preferenceManager: SharedPreferenceManager,
            @IoDispatcher dispatcher: CoroutineDispatcher,
        ): StationRepository {
            return StationRepositoryImpl(
                stationArrivalsApi,
                stationApi,
                appDatabase.dao(),
                preferenceManager,
                dispatcher
            )
        }
    }
}
