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
package com.keelim.nandadiagnosis.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.domain.theme.ThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeUseCase: ThemeUseCase,
) : ViewModel() {

    val theme: StateFlow<Int?> = themeUseCase.appTheme
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    fun setAppTheme(theme: Int) = viewModelScope.launch {
        themeUseCase.setUserTheme(theme)
    }
}
