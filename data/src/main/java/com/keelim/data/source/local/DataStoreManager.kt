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
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.keelim.data.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val defaultDataStore: DataStore<Preferences>,
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

    fun getUserId(keyStone: String): Flow<String> = defaultDataStore.data.map { preferences ->
        val key = stringPreferencesKey(keyStone)
        preferences[key] ?: "None"
    }

    suspend fun setUserId(keyStone: String, id: String) {
        val key = stringPreferencesKey(keyStone)
        defaultDataStore.edit { preferences ->
            preferences[key] = id
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
