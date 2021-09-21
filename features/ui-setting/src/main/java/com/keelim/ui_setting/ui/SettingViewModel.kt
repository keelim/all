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
import com.keelim.common.toUiState
import com.keelim.nandadiagnosis.compose.ui.UiState
import com.keelim.nandadiagnosis.domain.GetDevelopersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getDevelopers: GetDevelopersUseCase
) : ViewModel() {
    val developers = liveData {
        emit(UiState.loading())
        emit(getDevelopers().toUiState())
    }
}
