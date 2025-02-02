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
package com.keelim.core.data.source.local

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.core.string.Word
import com.keelim.data.repository.PreferenceManager
import com.keelim.model.RemindTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(
    @ApplicationContext val context: Context,
) : PreferenceManager {
    
    private val sharedPreferences =
        context.getSharedPreferences("preference", Activity.MODE_PRIVATE)

    override fun getString(key: String): String? =
        sharedPreferences.getString(key, null)

    override fun putString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun getLong(key: String): Long? {
        val value = sharedPreferences.getLong(key, INVALID_LONG_VALUE)

        return if (value == INVALID_LONG_VALUE) {
            null
        } else {
            value
        }
    }

    override fun putLong(key: String, value: Long) =
        sharedPreferences.edit { putLong(key, value) }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Word.KEY_REMIND)
    private val remindStartTime = stringPreferencesKey(Word.KEY_REMIND_START_TIME)
    private val remindEndTime = stringPreferencesKey(Word.KEY_REMIND_END_TIME)
    private val remindWorkingTime = stringPreferencesKey(Word.KEY_REMIND_WORKING_TIME)

    override suspend fun getRemindTime(): RemindTime = context.dataStore.data.map { pref ->
        RemindTime(
            startTime = pref[remindStartTime] ?: "0",
            workingTime = pref[remindWorkingTime] ?: "0",
            endTime = pref[remindEndTime] ?: "0",
        )
    }.first()

    override suspend fun setStartTime(value: String) {
        context.dataStore.edit { pref ->
            pref[remindStartTime] = value
        }
    }

    override suspend fun setWorkingTime(value: String) {
        context.dataStore.edit { pref ->
            pref[remindWorkingTime] = value
        }
    }

    override suspend fun setEndTime(value: String) {
        context.dataStore.edit { pref ->
            pref[remindEndTime] = value
        }
    }

    companion object {
        private const val INVALID_LONG_VALUE = Long.MIN_VALUE
    }
}
