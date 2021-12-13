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
package com.keelim.nandadiagnosis.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.keelim.nandadiagnosis.data.db.entity.History
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity
import com.keelim.nandadiagnosis.domain.GetSearchListUseCase
import com.keelim.nandadiagnosis.domain.HistoryUseCase
import com.keelim.nandadiagnosis.domain.favorite.FavoriteUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val getSearchListUseCase: GetSearchListUseCase,
  private val favoriteUpdateUseCase: FavoriteUpdateUseCase,
  private val historyUseCase: HistoryUseCase,
) : ViewModel() {
  private val _state: MutableStateFlow<SearchListState> = MutableStateFlow(SearchListState.UnInitialized)
  val state: StateFlow<SearchListState> = _state

  private val _historyList = MutableLiveData<List<History>>(listOf())
  val historyList: LiveData<List<History>> get() = _historyList

  private val _history: MutableStateFlow<List<History>> = MutableStateFlow(emptyList())
  val history: StateFlow<List<History>> = _history

  fun search2(keyword: String?) = viewModelScope.launch {
    _state.emit(SearchListState.Loading)
    runCatching {
      getSearchListUseCase(keyword.orEmpty())
    }.onSuccess {
      _state.emit(SearchListState.Searching(it))
    }.onFailure {
      _state.emit(SearchListState.Error)
    }
  }

  fun deleteHistory(keyword: String?) = viewModelScope.launch {
    historyUseCase.deleteHistory(keyword.orEmpty())
  }

  fun saveHistory(keyword: String?) = viewModelScope.launch {
    keyword?.let {
      historyUseCase.saveHistory(it)
    }
  }

  fun getAllHistories() = viewModelScope.launch {
    _historyList.postValue(historyUseCase.getAllHistory())
    _history.value = historyUseCase.getAllHistory()
  }

  fun favoriteUpdate(favorite: Int, id: Int) = viewModelScope.launch {
    favoriteUpdateUseCase.invoke(favorite, id)
  }

  fun getContent(query:String = ""): Flow<PagingData<NandaEntity>> {
    return getSearchListUseCase.getSearchFlow(query)
      .cachedIn(viewModelScope)
  }
}
