package com.keelim.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.data.model.MarketSchedule
import com.keelim.data.repository.MarketNotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "market_notification_prefs")

@Singleton
class MarketNotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : MarketNotificationRepository {

    override fun getSchedules(): Flow<List<MarketSchedule>> {
        return context.dataStore.data.map { preferences ->
            val schedulesJson = preferences[SCHEDULES_KEY]
            if (schedulesJson.isNullOrEmpty()) {
                getDefaultSchedules()
            } else {
                try {
                    json.decodeFromString<List<MarketSchedule>>(schedulesJson)
                } catch (e: Exception) {
                    getDefaultSchedules()
                }
            }
        }
    }

    override suspend fun saveSchedules(schedules: List<MarketSchedule>) {
        context.dataStore.edit { preferences ->
            preferences[SCHEDULES_KEY] = json.encodeToString(schedules)
        }
    }

    override suspend fun updateSchedule(schedule: MarketSchedule) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[SCHEDULES_KEY]
            val currentSchedules = if (currentJson.isNullOrEmpty()) {
                getDefaultSchedules()
            } else {
                try {
                    json.decodeFromString<List<MarketSchedule>>(currentJson)
                } catch (e: Exception) {
                    getDefaultSchedules()
                }
            }

            val updatedSchedules = currentSchedules.map {
                if (it.id == schedule.id) schedule else it
            }
            preferences[SCHEDULES_KEY] = json.encodeToString(updatedSchedules)
        }
    }

    override suspend fun addSchedule(schedule: MarketSchedule) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[SCHEDULES_KEY]
            val currentSchedules = if (currentJson.isNullOrEmpty()) {
                getDefaultSchedules()
            } else {
                try {
                    json.decodeFromString<List<MarketSchedule>>(currentJson)
                } catch (e: Exception) {
                    getDefaultSchedules()
                }
            }

            val updatedSchedules = currentSchedules + schedule
            preferences[SCHEDULES_KEY] = json.encodeToString(updatedSchedules)
        }
    }

    override suspend fun removeSchedule(scheduleId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[SCHEDULES_KEY]
            val currentSchedules = if (currentJson.isNullOrEmpty()) {
                getDefaultSchedules()
            } else {
                try {
                    json.decodeFromString<List<MarketSchedule>>(currentJson)
                } catch (e: Exception) {
                    getDefaultSchedules()
                }
            }

            val updatedSchedules = currentSchedules.filter { it.id != scheduleId }
            preferences[SCHEDULES_KEY] = json.encodeToString(updatedSchedules)
        }
    }

    private fun getDefaultSchedules(): List<MarketSchedule> {
        return listOf(
            MarketSchedule.KOREA_MARKET,
            MarketSchedule.US_MARKET_WINTER
        )
    }

    companion object {
        private val SCHEDULES_KEY = stringPreferencesKey("market_schedules")
    }
}
