package com.keelim.nandadiagnosis.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel:ViewModel() {
    private val _searchListState = MutableLiveData<SearchListState>(SearchListState.UnInitialized)
    val searchListState: LiveData<SearchListState> get() = _searchListState

    fun fetchData(): Job = viewModelScope.launch {
        setState(
            SearchListState.Loading
        )
    }

    fun setState(state: SearchListState){
        _searchListState.postValue(state)
    }
}