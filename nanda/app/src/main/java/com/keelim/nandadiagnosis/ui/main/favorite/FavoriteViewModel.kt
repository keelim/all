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
package com.keelim.nandadiagnosis.ui.main.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.domain.GetFavoriteListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteListUseCase: GetFavoriteListUseCase,
) : ViewModel() {
    private var _favoriteState = MutableStateFlow<FavoriteListState>(FavoriteListState.UnInitialized)
    val favoriteState: StateFlow<FavoriteListState> get() = _favoriteState

    fun fetchData(): Job = viewModelScope.launch {
        setState(
            FavoriteListState.Loading
        )
        setState(
            FavoriteListState.Success(
                getFavoriteListUseCase.invoke()
            )
        )
    }

    private fun setState(state: FavoriteListState) {
        _favoriteState.value = state
    }
}
