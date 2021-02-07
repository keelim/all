package com.keelim.nandadiagnosis.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.main.MainActivity
import timber.log.Timber
import java.util.*

class FirebaseServiceID : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.d("FCM Services onNewToken")
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {}

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { showNotification(it) }
    }

    private fun showNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = getString(R.string.my_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setContentIntent(pIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)

        getSystemService(NotificationManager::class.java).run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
                createNotificationChannel (channel)
            }
            notify (Date().time.toInt(), notificationBuilder.build())
        }
    }


}
