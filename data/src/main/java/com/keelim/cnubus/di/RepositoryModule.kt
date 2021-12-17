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

import com.keelim.cnubus.data.repository.setting.DeveloperRepository
import com.keelim.cnubus.data.repository.setting.DeveloperRepositoryImpl
import com.keelim.cnubus.data.repository.station.StationApi
import com.keelim.cnubus.data.repository.station.StationApiImpl
import com.keelim.cnubus.data.repository.station.StationRepository
import com.keelim.cnubus.data.repository.station.StationRepositoryImpl
import com.keelim.cnubus.data.repository.theme.ThemeRepository
import com.keelim.cnubus.data.repository.theme.ThemeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindsDeveloperRepository(
        repository: DeveloperRepositoryImpl,
    ): DeveloperRepository

    @Binds
    abstract fun bindsStationApiRepository(
        api: StationApiImpl,
    ): StationApi

    @Binds
    abstract fun bindsStationRepository(
        repository: StationRepositoryImpl,
    ): StationRepository

    @Binds
    abstract fun bindsThemeRepository(
        repository: ThemeRepositoryImpl
    ): ThemeRepository
}
