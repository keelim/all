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

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.keelim.nandadiagnosis.R

class TerminateService : LifecycleService() {

  override fun onCreate() {
    super.onCreate()
    createChannelIfNeed()
    startForeground(
      NOTIFICATION_ID,
      createNotification()
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    stopForeground(true)
  }

  private fun createChannelIfNeed() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)
        ?.createNotificationChannel(
          NotificationChannel(
            CHANNEL_ID,
            "채널 이름",
            NotificationManager.IMPORTANCE_LOW
          )
        )
    }
  }
  private fun createNotification(): Notification =
    NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(R.mipmap.ic_launcher)
      .build()

  companion object {
    private const val CHANNEL_ID = "CHANNEL_ID"
    private const val NOTIFICATION_ID = 777
  }
}
