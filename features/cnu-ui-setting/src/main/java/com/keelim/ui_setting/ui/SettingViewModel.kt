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
package com.keelim.ui_setting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.keelim.common.extensions.toUiState
import com.keelim.compose.ui.UiState
import com.keelim.domain.setting.DevelopersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.internal.NopCollector.emit

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getDevelopers: DevelopersUseCase,
) : ViewModel() {
    val developers = liveData {
        emit(UiState.loading())
        emit(getDevelopers().toUiState())
    }
    val clocks = liveData {
        emit(UiState.loading())
        emit(UiState.success("success"))
    }
}
