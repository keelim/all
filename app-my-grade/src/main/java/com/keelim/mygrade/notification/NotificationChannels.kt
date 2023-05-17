package com.keelim.mygrade.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.keelim.mygrade.R

object NotificationChannels {

    const val NOTICE = "NTC"
    const val EVENT = "EVT"

    internal fun initialize(application: Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.getNotificationManager()?.run {
                val notice = NotificationChannel(
                    NOTICE,
                    application.getString(R.string.notification_channel_notice),
                    NotificationManager.IMPORTANCE_HIGH,
                )
                val event = NotificationChannel(
                    EVENT,
                    application.getString(R.string.notification_channel_event),
                    NotificationManager.IMPORTANCE_HIGH,
                )
                createNotificationChannels(listOf(notice, event))
            }
        }
    }
}
