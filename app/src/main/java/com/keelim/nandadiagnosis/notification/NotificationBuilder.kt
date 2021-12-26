package com.keelim.nandadiagnosis.notification

import androidx.core.app.NotificationCompat

interface NotificationBuilder {
    fun showNotification(action: NotificationCompat.Action?)
}