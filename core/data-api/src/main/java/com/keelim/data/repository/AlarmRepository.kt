package com.keelim.data.repository

import com.keelim.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun insertAlarm(title: String, subTitle: String): Boolean
    fun getAlarms(): Flow<List<Alarm>>
}
