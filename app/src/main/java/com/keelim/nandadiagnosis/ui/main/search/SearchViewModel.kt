package com.keelim.nandadiagnosis.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.data.db.entity.History
import com.keelim.nandadiagnosis.usecase.GetSearchListUseCase
import com.keelim.nandadiagnosis.usecase.history.DeleteHistoryUseCase
import com.keelim.nandadiagnosis.usecase.history.GetAllHistoryUseCase
import com.keelim.nandadiagnosis.usecase.history.SaveHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchListUseCase: GetSearchListUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase,
    private val getAllHistoryUseCase: GetAllHistoryUseCase,
) : ViewModel() {
    private val _searchListState = MutableLiveData<SearchListState>(SearchListState.UnInitialized)
    val searchListState: LiveData<SearchListState> get() = _searchListState

    private val _historyList = MutableLiveData<List<History>>(listOf())
    val historyList: LiveData<List<History>> get() = _historyList

    fun fetchData(): Job = viewModelScope.launch {
        setState(
            SearchListState.Loading
        )
    }

    fun search(keyword: String?): Job = viewModelScope.launch {
        Timber.d("${getSearchListUseCase.invoke(keyword.orEmpty())}")
        setState(
            SearchListState.Searching(
                getSearchListUseCase.invoke(keyword.orEmpty())
            )
        )
    }

    fun deleteHistory(keyword:String?) = viewModelScope.launch {
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
}