package com.keelim.cnubus.feature.map.ui.map3.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(

): ViewModel(){
    private val _state:MutableStateFlow<DetailState.UnInitialized> = MutableStateFlow(DetailState.UnInitialized)
    val state:StateFlow<DetailState>
        get() = _state

    init {

    }

    fun init() = viewModelScope.launch {

    }
}