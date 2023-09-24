/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.keelim.data.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    val defaultDataStore: DataStore<Preferences>,
    private val userSettingsDataStore: DataStore<UserSettings>,
) {
    fun getLong(keyStone: String): Flow<Long> = defaultDataStore.data.map { preferences ->
        val key = longPreferencesKey(keyStone)
        preferences[key] ?: 0L
    }

    suspend fun setLong(keyStone: String, time: Long) {
        val key = longPreferencesKey(keyStone)
        defaultDataStore.edit { preferences ->
            preferences[key] = time
        }
    }

    inline fun <reified T> getValue(key: String): Flow<T?> = defaultDataStore.data.map { preferences ->
        when(T::class) {
            Boolean::class -> preferences[booleanPreferencesKey(key)]
            Double::class -> preferences[doublePreferencesKey(key)]
            Float::class -> preferences[floatPreferencesKey(key)]
            Int::class -> preferences[intPreferencesKey(key)]
            String::class -> preferences[stringPreferencesKey(key)]
            else -> throw IllegalStateException()
        } as T
    }
    suspend fun setKeyValue(keyStone: String, value: Boolean) {
        val key = booleanPreferencesKey(keyStone)
        defaultDataStore.edit { preferences ->
            preferences[key] = value
        }
    }
    suspend fun setKeyValue(keyStone: String, value: Double) {
        val key = doublePreferencesKey(keyStone)
        defaultDataStore.edit { preferences ->
            preferences[key] = value
        }
    }
    suspend fun setKeyValue(keyStone: String, value: Float) {
        val key = floatPreferencesKey(keyStone)
        defaultDataStore.edit { preferences ->
            preferences[key] = value
        }
    }
    suspend fun setKeyValue(keyStone: String, value: Int) {
        val key = intPreferencesKey(keyStone)
        defaultDataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun setKeyValue(keyStone: String, value: String) {
        val key = stringPreferencesKey(keyStone)
        defaultDataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getUserSettingsFlow(): Flow<UserSettings> = userSettingsDataStore.data

    suspend fun setUserSettings() {
        try {
            userSettingsDataStore.updateData {
                UserSettings.newBuilder()
                    .setIsFirstUser(true)
                    .build()
            }
        } catch (throwable: Throwable) {
            Timber.e(throwable)
        }
    }
}
