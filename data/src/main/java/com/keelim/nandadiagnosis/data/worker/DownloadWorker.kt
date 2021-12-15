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
package com.keelim.nandadiagnosis.data.worker

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.keelim.nandadiagnosis.data.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.io.File

@HiltWorker
class DownloadWorker @AssistedInject constructor(
  @Assisted context: Context,
  @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
  override suspend fun doWork(): Result = with(Dispatchers.IO) {
    setForeground(createForegroundInfo())
    delay(500)
    (applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).run {
      enqueue(
        DownloadManager.Request(Uri.parse(applicationContext.getString(R.string.db_path)))
          .setTitle("Downloading")
          .setDescription("Downloading Database file")
          .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
          .setDestinationUri(Uri.fromFile(File(applicationContext.getExternalFilesDir(null), "nanda.db")))
          .setAllowedOverMetered(true)
          .setAllowedOverRoaming(true)
      )
    }
    return Result.success(
      workDataOf(
        "db" to File(applicationContext.getExternalFilesDir(null), "nanda.db").toString()
      )
    )
  }

  private fun createForegroundInfo(): ForegroundInfo {
    val intent = WorkManager.getInstance(applicationContext)
      .createCancelPendingIntent(id)

    val notification = NotificationCompat.Builder(
      applicationContext, "workDownload"
    )
      .setContentTitle("Downloading Your Image")
      .setTicker("Downloading Your Image")
      .setSmallIcon(R.drawable.notification_action_background)
      .setOngoing(true)
      .addAction(android.R.drawable.ic_delete, "Cancel Download", intent)
    // 3
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createChannel(notification, "workDownload")
    }
    return ForegroundInfo(1, notification.build())
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createChannel(
    notificationBuilder: NotificationCompat.Builder,
    id: String
  ) {
    val notificationManager =
      applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as
        NotificationManager
    notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
    val channel = NotificationChannel(
      id,
      "NandaDiagnosis",
      NotificationManager.IMPORTANCE_HIGH
    )
    channel.description = "NandaDiagnosis Notifications"
    notificationManager.createNotificationChannel(channel)
  }
}
