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
package com.keelim.nandadiagnosis.ui.screen.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.domain.nandadiagnosis.FavoriteUpdateUseCase
import com.keelim.domain.nandadiagnosis.GetSearchListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val getSearchListUseCase: GetSearchListUseCase,
    private val favoriteUpdateUseCase: FavoriteUpdateUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SearchListState> =
        MutableStateFlow(SearchListState.UnInitialized)
    val state: StateFlow<SearchListState> = _state

    private val query: MutableStateFlow<String> = MutableStateFlow("")
    init {
        observeSearchState()
    }

    fun search2(keyword: String?) =
        viewModelScope.launch {
            _state.emit(SearchListState.Loading)
            runCatching { getSearchListUseCase(keyword.orEmpty()) }
                .onSuccess { _state.emit(SearchListState.Searching(it)) }
                .onFailure { _state.emit(SearchListState.Error) }
        }

    fun favoriteUpdate(favorite: Int, id: Int) =
        viewModelScope.launch { favoriteUpdateUseCase.invoke(favorite, id) }

    fun queryFilter(value: String) = viewModelScope.launch { query.emit(value) }

    private fun observeSearchState() {
        getSearchListUseCase.searchData
            .combine(query) { data, queryString ->
                if (queryString.isNotBlank()) {
                    data.filter { nandaEntity -> nandaEntity.domain_name.contains(queryString) }
                } else {
                    data
                }
            }
            .onStart { _state.emit(SearchListState.Loading) }
            .onEach { _state.emit(SearchListState.Success(it)) }
            .catch {
                it.printStackTrace()
                _state.emit(SearchListState.Error)
            }
            .launchIn(viewModelScope)
    }
}
