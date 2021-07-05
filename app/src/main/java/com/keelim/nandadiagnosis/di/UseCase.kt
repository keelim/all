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

import com.keelim.nandadiagnosis.data.repository.IORepository
import com.keelim.nandadiagnosis.ui.main.setting.theme.ThemeRepository
import com.keelim.nandadiagnosis.usecase.GetAppThemeUseCase
import com.keelim.nandadiagnosis.usecase.favorite.GetFavoriteListUseCase
import com.keelim.nandadiagnosis.usecase.GetNandaListUseCase
import com.keelim.nandadiagnosis.usecase.GetNandaUseCase
import com.keelim.nandadiagnosis.usecase.GetSearchListUseCase
import com.keelim.nandadiagnosis.usecase.SetAppThemeUseCase
import com.keelim.nandadiagnosis.usecase.history.DeleteHistoryUseCase
import com.keelim.nandadiagnosis.usecase.history.GetAllHistoryUseCase
import com.keelim.nandadiagnosis.usecase.history.SaveHistoryUseCase
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
  fun provideGetAllHistoryUseCase(ioRepository: IORepository) = GetAllHistoryUseCase(ioRepository)

  @Provides
  @ViewModelScoped
  fun provideSaveHistoryUseCase(ioRepository: IORepository) = SaveHistoryUseCase(ioRepository)

  @Provides
  @ViewModelScoped
  fun provideDeleteHistoryUseCase(ioRepository: IORepository) = DeleteHistoryUseCase(ioRepository)
}
