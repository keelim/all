package com.keelim.commonAndroid.platform.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.keelim.common.platform.notification.NotificationPolicy
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.notification.NotificationScheduleResult
import com.keelim.common.platform.notification.NotificationScheduler
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

class WorkManagerNotificationScheduler(
    private val context: Context,
    private val workManager: WorkManager = WorkManager.getInstance(context),
    private val now: () -> Instant = Instant::now,
    private val canPostNotifications: () -> Boolean = context::canPostNotifications,
) : NotificationScheduler {
    override suspend fun schedule(request: NotificationRequest): NotificationScheduleResult {
        val violations = NotificationPolicy.violations(request)
        if (violations.isNotEmpty()) return NotificationScheduleResult.Invalid(violations)
        if (!canPostNotifications()) return NotificationScheduleResult.PermissionDenied

        val publicContent = NotificationPolicy.publicContent(request.content)
        val work = OneTimeWorkRequestBuilder<PlatformNotificationWorker>()
            .setInitialDelay(
                Duration.between(now(), request.scheduledAt).coerceAtLeast(Duration.ZERO).toMillis(),
                TimeUnit.MILLISECONDS,
            )
            .setInputData(
                Data.Builder()
                    .putString(KEY_ID, request.id)
                    .putString(KEY_CHANNEL_ID, request.channel.id)
                    .putString(KEY_CHANNEL_NAME, request.channel.name)
                    .putString(KEY_CHANNEL_DESCRIPTION, request.channel.description)
                    .putString(KEY_TITLE, request.content.title)
                    .putString(KEY_BODY, request.content.body)
                    .putString(KEY_PUBLIC_TITLE, publicContent.publicTitle)
                    .putString(KEY_PUBLIC_BODY, publicContent.publicBody)
                    .build(),
            )
            .addTag(PLATFORM_TAG)
            .apply { request.tag?.let(::addTag) }
            .build()
        workManager.enqueueUniqueWork(uniqueName(request.id), ExistingWorkPolicy.REPLACE, work)
        return NotificationScheduleResult.Scheduled
    }

    override suspend fun cancel(id: String) {
        workManager.cancelUniqueWork(uniqueName(id))
    }

    override suspend fun cancelByTag(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    private fun uniqueName(id: String) = "keelim.platform.notification.$id"
}

class PlatformNotificationWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (!applicationContext.canPostNotifications()) return Result.failure()
        val channelId = inputData.getString(KEY_CHANNEL_ID) ?: return Result.failure()
        val channelName = inputData.getString(KEY_CHANNEL_NAME) ?: return Result.failure()
        val channelDescription = inputData.getString(KEY_CHANNEL_DESCRIPTION).orEmpty()
        val id = inputData.getString(KEY_ID) ?: return Result.failure()

        val manager = applicationContext.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
                .apply { description = channelDescription },
        )

        val launchIntent = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)
        val pendingIntent = launchIntent?.let {
            PendingIntent.getActivity(
                applicationContext,
                id.hashCode(),
                it,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
        }
        val publicVersion = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(applicationContext.applicationInfo.icon)
            .setContentTitle(inputData.getString(KEY_PUBLIC_TITLE))
            .setContentText(inputData.getString(KEY_PUBLIC_BODY))
            .build()
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(applicationContext.applicationInfo.icon)
            .setContentTitle(inputData.getString(KEY_TITLE))
            .setContentText(inputData.getString(KEY_BODY))
            .setPublicVersion(publicVersion)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setAutoCancel(true)
            .apply { pendingIntent?.let(::setContentIntent) }
            .build()
        NotificationManagerCompat.from(applicationContext).notify(id.hashCode(), notification)
        return Result.success()
    }
}

private fun Context.canPostNotifications(): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED

private const val PLATFORM_TAG = "keelim.platform.notifications"
private const val KEY_ID = "id"
private const val KEY_CHANNEL_ID = "channel_id"
private const val KEY_CHANNEL_NAME = "channel_name"
private const val KEY_CHANNEL_DESCRIPTION = "channel_description"
private const val KEY_TITLE = "title"
private const val KEY_BODY = "body"
private const val KEY_PUBLIC_TITLE = "public_title"
private const val KEY_PUBLIC_BODY = "public_body"
