package com.keelim.core.data.source.alarm

import com.keelim.data.repository.AlarmRepository
import com.keelim.model.Alarm
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(

): AlarmRepository {
    override suspend fun insertAlarm() {
        TODO("Not yet implemented")
    }

    override suspend fun getAlarms(): List<Alarm> {
        TODO("Not yet implemented")
    }
}
