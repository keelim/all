package com.keelim.data.repository

import com.keelim.model.Alarm

interface AlarmRepository {
    suspend fun insertAlarm()
    suspend fun getAlarms(): List<Alarm>
}
