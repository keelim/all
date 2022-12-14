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
package com.keelim.domain.di

import com.keelim.data.repository.NandaIORepository
import com.keelim.data.repository.theme.ThemeRepository
import com.keelim.domain.nandadiagnosis.GetFavoriteListUseCase
import com.keelim.domain.nandadiagnosis.GetNandaListUseCase
import com.keelim.domain.nandadiagnosis.GetNandaUseCase
import com.keelim.domain.nandadiagnosis.GetSearchListUseCase
import com.keelim.domain.nandadiagnosis.HistoryUseCase
import com.keelim.domain.theme.ThemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetNandaUseCase(nandaIoRepository: NandaIORepository) = GetNandaUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNandaListUseCase(nandaIoRepository: NandaIORepository) = GetNandaListUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideGetFavoriteListUseCase(nandaIoRepository: NandaIORepository) =
        GetFavoriteListUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSearchListUseCase(nandaIoRepository: NandaIORepository) = GetSearchListUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideHistoryUseCase(nandaIoRepository: NandaIORepository) = HistoryUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideThemeUseCase(
        themeRepository: ThemeRepository
    ): ThemeUseCase = ThemeUseCase(themeRepository)
}
