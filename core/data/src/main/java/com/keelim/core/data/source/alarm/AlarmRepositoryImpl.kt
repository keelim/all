package com.keelim.core.data.source.alarm

import com.keelim.core.database.mapper.toAlarm
import com.keelim.core.database.mapper.toAlarmEntity
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import com.keelim.data.repository.AlarmRepository
import com.keelim.model.Alarm
import com.keelim.shared.data.database.dao.AlarmDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val localDataSource: AlarmDao,
    @Dispatcher(KeelimDispatchers.IO) private val io: CoroutineDispatcher,
) : AlarmRepository {
    override suspend fun insertAlarm(
        title: String,
        subTitle: String,
    ): Boolean {
        return try {
            withContext(io) {
                localDataSource.upsert(
                    Alarm(
                        title = title,
                        subTitle = subTitle,
                    ).toAlarmEntity(),
                )
            }
            true
        } catch (throwable: Throwable) {
            Timber.e(throwable.message.toString())
            false
        }
    }

    override fun getAlarms(): Flow<List<Alarm>> = localDataSource.getAllAlarms()
        .map { it.toAlarm() }
}
