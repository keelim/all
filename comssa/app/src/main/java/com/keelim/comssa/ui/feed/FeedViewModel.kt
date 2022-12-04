package com.keelim.comssa.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedViewModel @Inject constructor() : ViewModel() {
    private val _state: MutableStateFlow<FeedState> = MutableStateFlow(FeedState.UnInitialized)
    val state: StateFlow<FeedState> get() = _state

    init {
        init()
    }

    private fun init() = viewModelScope.launch {
        _state.emit(FeedState.Loading)
        runCatching {
//            feedUseCase.getFeed()
        }.onSuccess {
            _state.emit(FeedState.Success(emptyList()))
        }.onFailure {
            _state.emit(FeedState.Error)
        }
    }
}
