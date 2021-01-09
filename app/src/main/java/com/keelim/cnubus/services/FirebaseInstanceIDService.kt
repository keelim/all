package com.keelim.cnubus.services


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.MainActivity

class FirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) { // 메시지 수신 시 실행되는 메소드
        sendNotification(remoteMessage)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val message = remoteMessage.data["data"]
        // 수신되는 푸시 메시지
//        val messageDivider = message!!.indexOf("|")
//        // 구분자를 통해 어떤 종류의 알람인지를 구별합니다.
//
//        val pushType = message.substring(messageDivider + 1) // 구분자 뒤에 나오는 메시지
        val resultIntent = Intent(this, MainActivity::class.java)
        resultIntent.putExtra("pushType", "pushType")

        val pendingIntent =
            PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("CnuBus")
                .setContentText("Arrive the message")
                .setAutoCancel(true)
                .setColor(Color.parseColor("#0ec874")) // 푸시 색상
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 버전 제약을 두는 것
            val channel = "cnubus"
            val channelName = "C" // 앱 설정에서 알림 이름으로 뜸.
            val notificationChannel =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelMessage =
                NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            channelMessage.apply {
                description = "this channel is about cnubus"
                enableLights(true)
                enableVibration(true)
                setShowBadge(false)
                vibrationPattern = longArrayOf(100, 200, 100, 200)
            }
            notificationChannel.createNotificationChannel(channelMessage)
            notificationBuilder.setChannelId(channel)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(9999, notificationBuilder.build())
    }
}