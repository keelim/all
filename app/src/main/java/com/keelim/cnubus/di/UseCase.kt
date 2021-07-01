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
package com.keelim.cnubus.di

import com.keelim.cnubus.ui.setting.theme.ThemeRepository
import com.keelim.cnubus.usecase.GetAppThemeUseCase
import com.keelim.cnubus.usecase.SetAppThemeUseCase
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
    fun provideAppThemeUseCase(themeRepository: ThemeRepository): GetAppThemeUseCase {
        return GetAppThemeUseCase(themeRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSetAppThemeUseCase(themeRepository: ThemeRepository): SetAppThemeUseCase {
        return SetAppThemeUseCase(themeRepository)
    }
}
