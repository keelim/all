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
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.keelim.nandadiagnosis.data.R
import java.io.File
import kotlinx.coroutines.delay


class DownloadWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    ): CoroutineWorker(context, workerParameters){
    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())
        delay(500)
        val downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(
            DownloadManager.Request(Uri.parse(applicationContext.getString(R.string.db_path)))
                .setTitle("Downloading")
                .setDescription("Downloading Database file")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(File(applicationContext.getExternalFilesDir(null), "nanda.db")))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        )
        return Result.success(workDataOf(
            "db" to File(applicationContext.getExternalFilesDir(null), "nanda.db").toString()
        ))
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        val notification = NotificationCompat.Builder(
            applicationContext, "workDownload")
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