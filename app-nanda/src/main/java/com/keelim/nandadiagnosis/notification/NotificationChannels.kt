package com.keelim.nandadiagnosis.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.keelim.nandadiagnosis.R

object NotificationChannels {
    const val NOTICE = "NOTIFICATION"

    internal fun initialize(application: Application) {
        application.getNotificationManager()?.run {
            val notice = NotificationChannel(
                NOTICE,
                application.getString(R.string.notification_channel_notice),
                NotificationManager.IMPORTANCE_HIGH,
            )
            createNotificationChannels(listOf(notice))
        }
    }
}
