package com.keelim.setting.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {
    // repository 만들 것
    val notificationState = flow {
        emit(NotificationState.Empty)
    }.catch {
        emit(NotificationState.Empty)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), NotificationState.Empty)
}


data class Notification(
    val title: String,
    val desc: String,
    val imageUrl: String
)

sealed interface NotificationState {
    object Empty : NotificationState
    class Success(val items: PersistentList<Notification>) : NotificationState
}
