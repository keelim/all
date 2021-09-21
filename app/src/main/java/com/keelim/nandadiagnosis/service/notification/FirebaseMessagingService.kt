/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.nandadiagnosis.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.main.Main2Activity
import java.util.*
import timber.log.Timber

class FirebaseMessagingService : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
    Timber.d("FCM Services onNewToken")
    sendTokenToServer(token)
  }

  private fun sendTokenToServer(token: String) {
    // TODO: implementation Server 에서 갱신해야 하는 부분
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    Timber.d("From: ${remoteMessage.from}")

    if (remoteMessage.data.isNotEmpty()) {
      remoteMessage.notification?.let { showNotification(it) }
    }

    val title = remoteMessage.data["title"]
    val message = remoteMessage.data["message"]
    val type = remoteMessage.data["type"]?.let {
      NotificationType.valueOf(it)
    } ?: return

    createNotificationChannel()

    NotificationManagerCompat.from(this)
      .notify(type.id, createNotification(type, title, message))
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
      )
      channel.description = CHANNEL_DESCRIPTION

      (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
        channel
      )
    }
  }

  private fun createNotification(
    type: NotificationType,
    title: String?,
    message: String?,
  ): Notification {
    val pendingIntent = PendingIntent.getActivity(
      this,
      type.id,
      Intent(this, Main2Activity::class.java).apply {
        putExtra("notificationType", "${type.title} 타입")
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
      },
      PendingIntent.FLAG_MUTABLE or FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle(title)
      .setContentText(message)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)

    when (type) {
      NotificationType.NORMAL -> Unit

      NotificationType.EXPANDABLE -> {
        notificationBuilder.setStyle(
          NotificationCompat.BigTextStyle()
            .bigText("난다진단이 실행 중 입니다.")
        )
      }

      NotificationType.CUSTOM -> {
        notificationBuilder
          .setStyle(NotificationCompat.DecoratedCustomViewStyle())
          .setCustomContentView(
            RemoteViews(
              packageName,
              R.layout.notification_message
            ).apply {
              setTextViewText(R.id.title, title)
              setTextViewText(R.id.message, message)
            }
          )
      }
    }
    return notificationBuilder.build()
  }

  private fun showNotification(notification: RemoteMessage.Notification) {
    val intent = Intent(this, Main2Activity::class.java)
    val pIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT)
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
        createNotificationChannel(channel)
      }
      notify(Date().time.toInt(), notificationBuilder.build())
    }
  }

  companion object {
    private const val CHANNEL_NAME = "Nanda"
    private const val CHANNEL_DESCRIPTION = "Nanda for channel"
    private const val CHANNEL_ID = "CHANNEL ID"
  }
}
