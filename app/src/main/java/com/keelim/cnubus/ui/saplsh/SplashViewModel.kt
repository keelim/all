package com.keelim.cnubus.ui.saplsh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    init {
        viewModelScope.launch {
            delay(1_000L)
            _loading.value = true
        }
    }
}