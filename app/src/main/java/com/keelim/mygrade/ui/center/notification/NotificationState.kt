package com.keelim.mygrade.ui.center.notification

import com.keelim.mygrade.data.Release


sealed class NotificationState {
    object UnInitialized : NotificationState()
    object Loading : NotificationState()
    data class Error(
        val message: String
    ) : NotificationState()
    data class Success(
        val data: List<Release>
    ) : NotificationState()
}
