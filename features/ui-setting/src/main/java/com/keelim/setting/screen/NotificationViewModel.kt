package com.keelim.setting.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.di.DefaultDispatcher
import com.keelim.data.source.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
@Inject
constructor(
    val notificationRepository: NotificationRepository,
    @DefaultDispatcher val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    // repository 만들 것
    val notificationState =
        flow {
            notificationRepository
                .getNotification()
                .map { Notification(it.date, it.title, it.desc) }
                .let {
                    if (it.isEmpty()) {
                        emit(NotificationState.Empty)
                    } else {
                        emit(NotificationState.Success(it.toPersistentList()))
                    }
                }
        }
            .flowOn(dispatcher)
            .catch { emit(NotificationState.Empty) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), NotificationState.Empty)
}

data class Notification(val date: String, val title: String, val desc: String)

sealed interface NotificationState {
    object Empty : NotificationState
    class Success(val items: PersistentList<Notification>) : NotificationState
}
