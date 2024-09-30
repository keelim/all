package com.keelim.setting.screen.notification

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class NotificationViewModel
@Inject
constructor(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    val notificationState =
        flow {
            notificationRepository
                .getNotification()
                .map { Notification(it.date, it.title, it.desc, it.fixed) }
                .partition { it.fixed }
                .let { (fixed, general) ->
                    if (fixed.isEmpty() && general.isEmpty()) {
                        emit(NotificationState.Empty)
                    } else {
                        emit(
                            NotificationState.Success(
                                fixedItems = fixed.toPersistentList(),
                                generalItems = general.toPersistentList(),
                            ),
                        )
                    }
                }
        }
            .catch { emit(NotificationState.Empty) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L),
                NotificationState.Empty,
            )
}

data class Notification(val date: String, val title: String, val desc: String, val fixed: Boolean)

sealed interface NotificationState {
    data object Empty : NotificationState
    class Success(
        val fixedItems: PersistentList<Notification>,
        val generalItems: PersistentList<Notification>,
    ) : NotificationState
}
