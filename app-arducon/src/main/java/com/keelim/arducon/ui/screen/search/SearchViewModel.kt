package com.keelim.arducon.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.data.repository.ArduconRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ArduconRepository,
    @Dispatcher(KeelimDispatchers.DEFAULT) private val default: CoroutineDispatcher,
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val schemeList: StateFlow<List<String>> = repository.getSchemeList()
        .flowOn(default)
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000L), emptyList())
    
    val filteredSchemes: StateFlow<List<String>> = combine(
        schemeList,
        _searchQuery
    ) { schemes, query ->
        if (query.isEmpty()) {
            schemes
        } else {
            schemes.filter { scheme ->
                scheme.contains(query, ignoreCase = true)
            }
        }
    }
    .flowOn(default)
    .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000L), emptyList())
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
    }
}
