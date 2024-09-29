package com.keelim.data.repository

import com.keelim.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun insertAlarm(alarm: Alarm): Boolean
    fun getAlarms(): Flow<List<Alarm>>
}
