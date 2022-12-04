package com.keelim.nandadiagnosis.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.keelim.nandadiagnosis.notification.NotificationBuilder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class MainWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
    private val notificationBuilder: NotificationBuilder,
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            notificationBuilder.showNotification(null)
            Result.success()
        } catch (t: Throwable) {
            if (runAttemptCount <= 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
    companion object {

        private const val DEBUG = false
        private const val TAG = "MainWorker"

        fun enqueueWork(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, createRequest())
        }

        fun cancelWork(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(TAG)
        }

        private fun createRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MainWorker>()
                .setInitialDelay(36000000000, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .build()
        }
    }
}
