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
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
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
import com.keelim.commonAndroid.util.DownloadReceiver
import com.keelim.core.resource.Res
import com.keelim.core.resource.nanda_download_cancel_action
import com.keelim.core.resource.nanda_download_channel_description
import com.keelim.core.resource.nanda_download_channel_name
import com.keelim.core.resource.nanda_download_description
import com.keelim.core.resource.nanda_download_failed_toast
import com.keelim.core.resource.nanda_download_foreground_ticker
import com.keelim.core.resource.nanda_download_foreground_title
import com.keelim.core.resource.nanda_download_start_toast
import com.keelim.core.resource.nanda_download_title
import com.keelim.core.resource.nanda_download_url
import com.keelim.nandadiagnosis.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

@HiltWorker
@OptIn(ExperimentalResourceApi::class)
class DownloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    val receiver: DownloadReceiver,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val downloadUrl = getString(Res.string.nanda_download_url)
            val downloadTitle = getString(Res.string.nanda_download_title)
            val downloadDescription = getString(Res.string.nanda_download_description)
            setForeground(createForegroundInfo())
            (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).run {
                enqueue(
                    DownloadManager.Request(downloadUrl.toUri())
                        .setTitle(downloadTitle)
                        .setDescription(downloadDescription)
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

    private suspend fun createForegroundInfo(): ForegroundInfo {
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)
        val foregroundTitle = getString(Res.string.nanda_download_foreground_title)
        val foregroundTicker = getString(Res.string.nanda_download_foreground_ticker)
        val cancelAction = getString(Res.string.nanda_download_cancel_action)
        val channelName = getString(Res.string.nanda_download_channel_name)
        val channelDescription = getString(Res.string.nanda_download_channel_description)

        val notification = NotificationCompat.Builder(
            applicationContext,
            "workDownload",
        )
            .setContentTitle(foregroundTitle)
            .setTicker(foregroundTicker)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancelAction, intent)
        // 3
        createChannel(
            notificationBuilder = notification,
            id = "workDownload",
            channelName = channelName,
            channelDescription = channelDescription
        )
        return ForegroundInfo(1, notification.build())
    }

    private fun createChannel(
        notificationBuilder: NotificationCompat.Builder,
        id: String,
        channelName: String,
        channelDescription: String
    ) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
        val channel = NotificationChannel(
            id,
            channelName,
            NotificationManager.IMPORTANCE_HIGH,
        )
        channel.description = channelDescription
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
                            WorkInfo.State.ENQUEUED -> {
                                ctx.toast(resolveString(Res.string.nanda_download_start_toast))
                            }
                            WorkInfo.State.RUNNING -> Unit
                            WorkInfo.State.SUCCEEDED -> Unit
                            WorkInfo.State.FAILED -> {
                                ctx.toast(resolveString(Res.string.nanda_download_failed_toast))
                            }
                            WorkInfo.State.BLOCKED -> {
                                ctx.toast(resolveString(Res.string.nanda_download_failed_toast))
                            }
                            WorkInfo.State.CANCELLED -> {
                                ctx.toast(resolveString(Res.string.nanda_download_failed_toast))
                            }
                        }
                    }
                }
        }

        private fun resolveString(resource: StringResource): String = runBlocking {
            getString(resource)
        }
    }
}
