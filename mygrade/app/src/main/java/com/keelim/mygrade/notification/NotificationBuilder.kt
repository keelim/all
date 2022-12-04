package com.keelim.mygrade.notification

import androidx.core.app.NotificationCompat

interface NotificationBuilder {
    fun showNotification(action: NotificationCompat.Action?)
}
