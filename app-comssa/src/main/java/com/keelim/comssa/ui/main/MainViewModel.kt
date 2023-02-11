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
package com.keelim.comssa.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val downloadUrl: String,
) {
    companion object {
        val UnInitialize = MainUiState("")
    }
}

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _mainUiState: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState.UnInitialize)

    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()


    fun getDownloadLink(password: String) = viewModelScope.launch {
        runCatching {}.onSuccess {}
    }
}
