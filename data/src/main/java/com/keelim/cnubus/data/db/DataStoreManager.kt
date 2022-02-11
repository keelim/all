package com.keelim.cnubus.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.keelim.cnubus.data.db.DataStoreManager.PreferencesKeys.KEY_LAST_DATABASE_UPDATED_TIME_MILLIS
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun getLong(): Flow<Long> = dataStore.data.map { preferences ->
        preferences[KEY_LAST_DATABASE_UPDATED_TIME_MILLIS] ?: 0L
    }

    suspend fun setLong(time: Long) {
        dataStore.edit { preferences ->
            preferences[KEY_LAST_DATABASE_UPDATED_TIME_MILLIS] = time
        }
    }

    private object PreferencesKeys {
        val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = longPreferencesKey(LAST_DATABASE_ACCESS_KEY)
    }

    companion object {
        private const val LAST_DATABASE_ACCESS_KEY = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
    }
}