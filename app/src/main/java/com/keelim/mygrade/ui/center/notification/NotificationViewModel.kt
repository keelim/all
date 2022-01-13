package com.keelim.mygrade.ui.center.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.IoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val ioRepository: IoRepository,
) : ViewModel() {
    private var _state: MutableStateFlow<NotificationState> = MutableStateFlow(NotificationState.UnInitialized)
    val state: StateFlow<NotificationState> = _state

    fun fetchRelease() = viewModelScope.launch {
        _state.emit(NotificationState.Loading)
        runCatching {
            ioRepository.getNotification()
        }.onSuccess {
            _state.emit(NotificationState.Success(it))
        }.onFailure {
            _state.emit(NotificationState.Error("에러가 발생하였습니다."))
        }
    }
}
