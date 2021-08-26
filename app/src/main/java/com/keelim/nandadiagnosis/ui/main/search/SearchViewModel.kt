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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
<<<<<<< Updated upstream
import com.keelim.nandadiagnosis.data.db.entity.History
=======
>>>>>>> Stashed changes
import com.keelim.nandadiagnosis.domain.GetSearchListUseCase
import com.keelim.nandadiagnosis.domain.favorite.FavoriteUpdateUseCase
import com.keelim.nandadiagnosis.domain.history.DeleteHistoryUseCase
import com.keelim.nandadiagnosis.domain.history.GetAllHistoryUseCase
import com.keelim.nandadiagnosis.domain.history.SaveHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val getSearchListUseCase: GetSearchListUseCase,
  private val deleteHistoryUseCase: DeleteHistoryUseCase,
  private val saveHistoryUseCase: SaveHistoryUseCase,
  private val getAllHistoryUseCase: GetAllHistoryUseCase,
  private val favoriteUpdateUseCase: FavoriteUpdateUseCase,
) : ViewModel() {
  private val _searchListState = MutableLiveData<SearchListState>(SearchListState.UnInitialized)
  val searchListState: LiveData<SearchListState> get() = _searchListState

  private val _historyList = MutableLiveData<List<History>>(listOf())
  val historyList: LiveData<List<History>> get() = _historyList

  val searchQuery = MutableStateFlow("")

  private val searchFlow = searchQuery.flatMapLatest {
    getSearchListUseCase.getFlowData(it)
  }

  val searchResult = searchFlow.asLiveData()

  fun fetchData(): Job = viewModelScope.launch {
    setState(
      SearchListState.Loading
    )
  }

  fun search(keyword: String?): Job = viewModelScope.launch {
    try {
      setState(
        SearchListState.Loading
      )

      setState(
        SearchListState.Searching(
          getSearchListUseCase.invoke(keyword.orEmpty())
        )
      )
    } catch (e:Exception){
      setState(
        SearchListState.Error
      )
    }
  }

  fun deleteHistory(keyword: String?) = viewModelScope.launch {
    deleteHistoryUseCase.invoke(keyword)
  }

  fun saveHistory(keyword: String?) = viewModelScope.launch {
    saveHistoryUseCase.invoke(keyword)
  }

  fun getAllHistories() = viewModelScope.launch {
    _historyList.postValue(getAllHistoryUseCase.invoke())
  }

  private fun setState(state: SearchListState) {
    _searchListState.postValue(state)
  }

  fun favoriteUpdate(favorite: Int, id: Int) = viewModelScope.launch {
    favoriteUpdateUseCase.invoke(favorite, id)
  }
}
