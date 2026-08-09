package com.keelim.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.core.data.source.local.StringPreferencesKeyMigration
import com.keelim.core.data.source.local.legacyDataStoreMigration
import com.keelim.data.model.MarketSchedule
import com.keelim.data.repository.MarketNotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

internal object MarketNotificationStore {
    const val STORE_NAME = "market_notification_preferences"
    const val LEGACY_STORE_NAME = "market_notification_prefs"
    const val LEGACY_SCHEDULES_KEY_NAME = "market_schedules"
    const val SCHEDULES_KEY_NAME = "market_notification_schedules"
}

internal val Context.marketNotificationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = MarketNotificationStore.STORE_NAME,
    produceMigrations = { context ->
        listOf(
            legacyDataStoreMigration(
                context = context,
                legacyStoreName = MarketNotificationStore.LEGACY_STORE_NAME,
                keyMigrations = listOf(
                    StringPreferencesKeyMigration(
                        oldKeyName = MarketNotificationStore.LEGACY_SCHEDULES_KEY_NAME,
                        newKeyName = MarketNotificationStore.SCHEDULES_KEY_NAME,
                    ),
                ),
            ),
        )
    },
)

@Singleton
class MarketNotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : MarketNotificationRepository {

    override fun getSchedules(): Flow<List<MarketSchedule>> {
        return context.marketNotificationDataStore.data.map { preferences ->
            val schedulesJson = preferences[schedulesKey]
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
        context.marketNotificationDataStore.edit { preferences ->
            preferences[schedulesKey] = json.encodeToString(schedules)
        }
    }

    override suspend fun updateSchedule(schedule: MarketSchedule) {
        context.marketNotificationDataStore.edit { preferences ->
            val currentJson = preferences[schedulesKey]
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
            preferences[schedulesKey] = json.encodeToString(updatedSchedules)
        }
    }

    override suspend fun addSchedule(schedule: MarketSchedule) {
        context.marketNotificationDataStore.edit { preferences ->
            val currentJson = preferences[schedulesKey]
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
            preferences[schedulesKey] = json.encodeToString(updatedSchedules)
        }
    }

    override suspend fun removeSchedule(scheduleId: String) {
        context.marketNotificationDataStore.edit { preferences ->
            val currentJson = preferences[schedulesKey]
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
            preferences[schedulesKey] = json.encodeToString(updatedSchedules)
        }
    }

    private fun getDefaultSchedules(): List<MarketSchedule> {
        return listOf(
            MarketSchedule.KOREA_MARKET,
            MarketSchedule.US_MARKET_WINTER
        )
    }

    private val schedulesKey = stringPreferencesKey(MarketNotificationStore.SCHEDULES_KEY_NAME)
}
