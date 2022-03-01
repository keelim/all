package com.keelim.cnubus.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun getLong(keyStone: String): Flow<Long> = dataStore.data.map { preferences ->
        val key = longPreferencesKey(keyStone)
        preferences[key] ?: 0L
    }

    suspend fun setLong(keyStone: String, time: Long) {
        val key = longPreferencesKey(keyStone)
        dataStore.edit { preferences ->
            preferences[key] = time
        }
    }

    fun getUserId(keyStone: String): Flow<String> = dataStore.data.map { preferences ->
        val key = stringPreferencesKey(keyStone)
        preferences[key] ?: "None"
    }

    suspend fun setUserId(keyStone: String, id: String) {
        val key = stringPreferencesKey(keyStone)
        dataStore.edit { preferences ->
            preferences[key] = id
        }
    }
}