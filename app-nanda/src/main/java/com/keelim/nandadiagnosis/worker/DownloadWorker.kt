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
package com.keelim.nandadiagnosis.worker

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
import androidx.lifecycle.LifecycleOwner
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.keelim.common.extensions.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.di.DownloadReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    val receiver: DownloadReceiver,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            setForeground(createForegroundInfo())
            delay(500)
            (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).run {
                enqueue(
                    DownloadManager.Request(Uri.parse(applicationContext.getString(R.string.db_path)))
                        .setTitle("Downloading")
                        .setDescription("Downloading Database file")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationUri(Uri.fromFile(File(applicationContext.getExternalFilesDir(null), "nanda.db")))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true),
                )
            }
            Result.success(
                workDataOf(
                    "db" to File(applicationContext.getExternalFilesDir(null), "nanda.db").toString(),
                ),
            )
        } catch (e: Exception) {
            if (runAttemptCount <= 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        val notification = NotificationCompat.Builder(
            applicationContext,
            "workDownload",
        )
            .setContentTitle("Downloading Your Image")
            .setTicker("Downloading Your Image")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, "Cancel Download", intent)
        // 3
        createChannel(notification, "workDownload")
        return ForegroundInfo(1, notification.build())
    }

    private fun createChannel(
        notificationBuilder: NotificationCompat.Builder,
        id: String,
    ) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
        val channel = NotificationChannel(
            id,
            "NandaDiagnosis",
            NotificationManager.IMPORTANCE_HIGH,
        )
        channel.description = "NandaDiagnosis Notifications"
        notificationManager.createNotificationChannel(channel)
    }

    companion object {

        private const val DEBUG = false
        private const val TAG = "DownloadWorker"
        private lateinit var request: OneTimeWorkRequest

        fun enqueueWork(context: Context, owner: LifecycleOwner) {
            request = createRequest()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, request)
            observeWork(owner, context, request.id)
        }

        fun cancelWork(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(TAG)
        }

        private fun createRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<DownloadWorker>()
                .setInitialDelay(36000000000, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresStorageNotLow(true)
                        .setRequiresBatteryNotLow(true)
                        .build(),
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .build()
        }

        private fun observeWork(owner: LifecycleOwner, ctx: Context, id: UUID) {
            WorkManager.getInstance(ctx).getWorkInfoByIdLiveData(id)
                .observe(owner) { info ->
                    // 2
                    info?.let {
                        when (it.state) {
                            WorkInfo.State.ENQUEUED -> ctx.toast("다운로드를 시작합니다.")
                            WorkInfo.State.RUNNING -> Unit
                            WorkInfo.State.SUCCEEDED -> Unit
                            WorkInfo.State.FAILED -> ctx.toast("다운로드를 완료하지 못했습니다..")
                            WorkInfo.State.BLOCKED -> ctx.toast("다운로드를 완료하지 못했습니다..")
                            WorkInfo.State.CANCELLED -> ctx.toast("다운로드를 완료하지 못했습니다..")
                        }
                    }
                }
        }
    }
}
