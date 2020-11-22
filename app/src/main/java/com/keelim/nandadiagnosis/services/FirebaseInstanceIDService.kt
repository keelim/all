package com.keelim.nandadiagnosis.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.main.MainActivity

class FirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.e("Firebase", "FirebaseInstanceIDService : $s")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) sendNotification(remoteMessage)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val message = remoteMessage.data["data"]
        // 수신되는 푸시 메시지

        // 구분자를 통해 어떤 종류의 알람인지를 구별합니다.

        val resultIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("pushType", "pushType")
        }
        val pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = "채널"
            val channelName = "채널 이름" // 앱 설정에서 알림 이름으로 뜸.
            val notiChannel = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelMessage = NotificationChannel(channel, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "채널에 대한 설명입니다."
                enableLights(true)
                enableVibration(true)
                setShowBadge(false)
                vibrationPattern = longArrayOf(100, 200, 100, 200)
            }

            notiChannel.createNotificationChannel(channelMessage)
            val notificationBuilder = NotificationCompat.Builder(this, channel)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("알림이 도착을 했습니다. ")
                    .setChannelId(channel)
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#0ec874"))
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(9999, notificationBuilder.build())

        } else {
            val notificationBuilder = NotificationCompat.Builder(this, "")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("푸시 타이틀")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#0ec874")) // 푸시 색상
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(9999, notificationBuilder.build())
        }
    }
}
