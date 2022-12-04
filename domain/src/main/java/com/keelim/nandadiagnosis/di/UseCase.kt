/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.nandadiagnosis.di

import com.keelim.data.repository.io.IORepository
import com.keelim.data.repository.theme.ThemeRepository
import com.keelim.nandadiagnosis.domain.GetAppThemeUseCase
import com.keelim.nandadiagnosis.domain.GetFavoriteListUseCase
import com.keelim.nandadiagnosis.domain.GetNandaListUseCase
import com.keelim.nandadiagnosis.domain.GetNandaUseCase
import com.keelim.nandadiagnosis.domain.GetSearchListUseCase
import com.keelim.nandadiagnosis.domain.HistoryUseCase
import com.keelim.nandadiagnosis.domain.SetAppThemeUseCase
import com.keelim.nandadiagnosis.domain.video.VideoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCase {
    @Provides
    @ViewModelScoped
    fun provideAppThemeUseCase(themeRepository: ThemeRepository) = GetAppThemeUseCase(themeRepository)

    @Provides
    @ViewModelScoped
    fun provideSetAppThemeUseCase(themeRepository: ThemeRepository) = SetAppThemeUseCase(themeRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNandaUseCase(ioRepository: IORepository) = GetNandaUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNandaListUseCase(ioRepository: IORepository) = GetNandaListUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideGetFavoriteListUseCase(ioRepository: IORepository) = GetFavoriteListUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSearchListUseCase(ioRepository: IORepository) = GetSearchListUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideHistoryUseCase(ioRepository: IORepository) = HistoryUseCase(ioRepository)

    @Provides
    @ViewModelScoped
    fun provideVideoUseCase(ioRepository: IORepository) = VideoUseCase(ioRepository)
}
