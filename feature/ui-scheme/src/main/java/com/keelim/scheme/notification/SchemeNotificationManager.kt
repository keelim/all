package com.keelim.scheme.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.keelim.scheme.SchemeActivity
import javax.inject.Inject

class SchemeNotificationManager @Inject constructor(
    private val context: Context,
) {

    private val notificationManager by lazy {
        context.getSystemService<NotificationManager>()
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = CHANNEL_DESCRIPTION
        }.also { channel ->
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showDeepLinkNotification(
        notificationId: Int,
        title: String,
        message: String,
        deepLinkUri: String,
    ) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            deepLinkUri.toUri(),
            context,
            SchemeActivity::class.java,
        ).apply {
            flags += Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(notificationId, notification)
    }

    companion object {
        const val CHANNEL_ID = "deep_link_channel"
        const val CHANNEL_NAME = "Deep Link Notifications"
        const val CHANNEL_DESCRIPTION = "Notifications for deep linking into the app"
    }
}
