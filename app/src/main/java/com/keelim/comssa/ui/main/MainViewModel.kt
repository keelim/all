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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.domain.SearchUseCase
import com.keelim.comssa.domain.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
) : ViewModel() {
    private val _mainListState = MutableLiveData<MainListState>(MainListState.UnInitialized)
    val mainListState: LiveData<MainListState> get() = _mainListState

    fun fetchData() = viewModelScope.launch {
        setState(
            MainListState.Loading
        )

        setState(
            MainListState.Success(
                listOf()
            )
        )
    }

    private fun setState(state: MainListState) {
        _mainListState.postValue(state)
    }

    fun search(keyword: String?) = viewModelScope.launch {
        setState(
            MainListState.Loading
        )

        setState(
            MainListState.Success(
                searchUseCase.invoke(keyword.orEmpty())
            )
        )
    }

    fun favorite(favorite: Int, id: Int) = viewModelScope.launch {
        when (favorite) {
            0 -> updateFavoriteUseCase.invoke(1, id)
            1 -> updateFavoriteUseCase.invoke(0, id)
        }
    }
}
