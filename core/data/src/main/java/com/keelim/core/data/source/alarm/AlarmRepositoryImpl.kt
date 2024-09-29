package com.keelim.core.data.source.alarm

import com.keelim.common.di.IoDispatcher
import com.keelim.core.database.dao.AlarmDao
import com.keelim.core.database.mapper.toAlarm
import com.keelim.core.database.mapper.toAlarmEntity
import com.keelim.data.repository.AlarmRepository
import com.keelim.model.Alarm
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val localDataSource: AlarmDao,
    @IoDispatcher private val io: CoroutineDispatcher,
): AlarmRepository {
    override suspend fun insertAlarm(alarm: Alarm): Boolean {
        return try {
            withContext(io) {
                localDataSource.upsert(alarm.toAlarmEntity())
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
