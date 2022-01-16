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
package com.keelim.cnubus.data.repository.theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    @ApplicationContext private val ctx: Context,
) : ThemeRepository {


    override fun getUserTheme() = ctx.dataStore.data.map { preferences ->
        preferences[USER_THEME] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    override suspend fun setUserTheme(theme: Int) {
        ctx.dataStore.edit { preferences ->
            preferences[USER_THEME] = theme
        }
    }

    companion object {
        private const val STORE_NAME = "preferences"
        private const val USER_THEME_KEY = "user_theme"
        val USER_THEME = intPreferencesKey(USER_THEME_KEY)
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)
    }
}
