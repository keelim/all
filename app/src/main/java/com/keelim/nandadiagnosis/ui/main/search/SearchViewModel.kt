package com.keelim.nandadiagnosis.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.usecase.GetSearchListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchListUseCase: GetSearchListUseCase,
) : ViewModel() {
    private val _searchListState = MutableLiveData<SearchListState>(SearchListState.UnInitialized)
    val searchListState: LiveData<SearchListState> get() = _searchListState

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

    private fun setState(state: SearchListState) {
        _searchListState.postValue(state)
    }
}