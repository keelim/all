package com.keelim.data.repository

import com.keelim.data.model.MarketSchedule
import kotlinx.coroutines.flow.Flow

interface MarketNotificationRepository {
    fun getSchedules(): Flow<List<MarketSchedule>>
    suspend fun saveSchedules(schedules: List<MarketSchedule>)
    suspend fun updateSchedule(schedule: MarketSchedule)
    suspend fun addSchedule(schedule: MarketSchedule)
    suspend fun removeSchedule(scheduleId: String)
}
