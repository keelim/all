/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
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
package com.keelim.cnubus.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.keelim.cnubus.data.repository.setting.DeveloperRepository
import com.keelim.cnubus.ui.main.MainActivity
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var repository: DeveloperRepository

    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(notificationManager)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d(remoteMessage.data.toString())
        if (remoteMessage.data.isNotEmpty()) {
            remoteMessage.data.apply {
                val nickname = get("title")!!
                val message = get("body")!!
                val busId = get("privateChatroomId")

                val pendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    UUID.randomUUID().hashCode(),
                    Intent(applicationContext, MainActivity::class.java).apply {
                        putExtra("busId", busId)
                    },
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    } else {
                        PendingIntent.FLAG_UPDATE_CURRENT
                    }
                    // PendingIntent.FLAG_UPDATE_CURRENT
                    // 기존 펜딩 인덴트가 있는 경우 교체
                    // Android SDK 31 Issue FLAG_IMMUTABLE
                )

                val notification = getNotificationBuilder(
                    nickname,
                    message,
                    busId!!,
                    pendingIntent
                ).build()
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }
    }

    override fun onNewToken(token: String) {
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        currentUser?.let {
            ProcessLifecycleOwner.get().lifecycle.coroutineScope.launch {
//                repository.updateToken(currentUser.uid, token)
            }
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "id",
                "CHATTING",
                IMPORTANCE_HIGH
            )
            channel.apply {
                enableLights(true)
                // setShowBadge(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "notification"
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    private fun getNotificationBuilder(
        title: String,
        content: String,
        privateChatroomId: String,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(
            this,
            "id"
        )
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setGroup(privateChatroomId)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setShowWhen(true)
    }

    companion object {
        const val NOTIFICATION_ID = 12345
    }
}
