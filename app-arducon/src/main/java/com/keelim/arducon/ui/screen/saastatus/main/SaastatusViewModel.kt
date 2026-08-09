package com.keelim.arducon.ui.screen.saastatus.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SaastatusItem(
    val title: String,
    val description: String,
)

@HiltViewModel
class SaastatusViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<List<SaastatusItem>>(emptyList())
    val state: StateFlow<List<SaastatusItem>> = _state.asStateFlow()
}
