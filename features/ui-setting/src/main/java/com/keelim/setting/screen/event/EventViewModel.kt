package com.keelim.setting.screen.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

}
