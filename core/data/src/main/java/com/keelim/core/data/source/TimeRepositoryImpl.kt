package com.keelim.core.data.source

import com.google.android.gms.tasks.Task
import com.google.android.gms.time.TrustedTimeClient
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.data.repository.TimeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TimeRepositoryImpl @Inject constructor(
    private val timeClient: Task<TrustedTimeClient>,
    @Dispatcher(KeelimDispatchers.IO) private val io: CoroutineDispatcher,
) : TimeRepository {
    override suspend fun getCurrentTime(): Long {
        return timeClient.await().computeCurrentUnixEpochMillis() ?: System.currentTimeMillis()
    }
}
