package com.keelim.cnubus.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class BusWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val loopInterval = inputData.getInt(START, 5)

        Timber.d("[HILTWORKER] WorkManager started, loop interval is $loopInterval")

        var loop = 0
        while (true) {
            Timber.d("[HILTWORKER] Loop number $loop")


            if (loop >= loopInterval) {
                break
            }
            Thread.sleep(1000)
            loop++
        }
        Timber.d("[HILTWORKER] Success")
        return@withContext Result.success()
    }

    companion object {
        const val START = "START"
    }
}
