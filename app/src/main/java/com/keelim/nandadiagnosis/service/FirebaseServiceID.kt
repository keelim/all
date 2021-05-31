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
import timber.log.Timber
import java.util.Date

class FirebaseServiceID : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
    Timber.d("FCM Services onNewToken")
    sendTokenToServer(token)
  }

  private fun sendTokenToServer(token: String) {
    // TODO: implementation
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    Timber.d("From: ${remoteMessage.from}")

    if (remoteMessage.data.isNotEmpty()) {
      remoteMessage.notification?.let { showNotification(it) }
    }
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
        createNotificationChannel(channel)
      }
      notify(Date().time.toInt(), notificationBuilder.build())
    }
  }
}
