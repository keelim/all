package com.keelim.mygrade.ui.center.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationViewModel @Inject constructor(
) : ViewModel() {
    private var _state: MutableStateFlow<NotificationState> = MutableStateFlow(NotificationState.UnInitialized)
    val state: StateFlow<NotificationState> = _state

    fun fetchRelease() = viewModelScope.launch {
        _state.emit(NotificationState.UnInitialized)

        runCatching {
        }.onSuccess {
        }.onFailure {
        }
    }
}
