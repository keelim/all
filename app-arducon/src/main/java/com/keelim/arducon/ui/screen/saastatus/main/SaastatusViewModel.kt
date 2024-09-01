package com.keelim.arducon.ui.screen.saastatus.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SaastatusItem(
    val title: String,
    val description: String,
)

sealed interface SaastatusState {
    data object Loading : SaastatusState
    data class Success(val items: List<SaastatusItem>) : SaastatusState
}

@HiltViewModel
class SaastatusViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SaastatusState>(SaastatusState.Loading)
    val state: StateFlow<SaastatusState> = _state.asStateFlow()

    init {
        _state.value = SaastatusState.Loading
        _state.value = SaastatusState.Success(listOf())
    }
}
