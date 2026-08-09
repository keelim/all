/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.core.data.source

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.core.data.source.ThemeRepository.PreferencesKeys.USER_THEME
import com.keelim.core.data.source.local.IntPreferencesKeyMigration
import com.keelim.core.data.source.local.SharedPreferencesIntKeyMigration
import com.keelim.core.data.source.local.SharedPreferencesStringKeyMigration
import com.keelim.core.data.source.local.StringPreferencesKeyMigration
import com.keelim.core.data.source.local.legacyDataStoreMigration
import com.keelim.core.data.source.local.sharedPreferencesMigration
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.map

internal object UserPreferencesStore {
    const val STORE_NAME = "user_preferences"
    const val LEGACY_DATASTORE_NAME = "preferences"
    const val LEGACY_SHARED_PREFERENCES_NAME = "preference"
    const val LEGACY_NANDA_SHARED_PREFERENCES_NAME = "com.keelim.nandaDiagnosis"

    const val USER_THEME_KEY_NAME = "user_theme"
    const val USER_JWT_TOKEN_KEY_NAME = "user_jwt_token"

    const val LEGACY_JWT_TOKEN_KEY_NAME = "jwt_token"
    const val LEGACY_NANDA_ID_TOKEN_KEY_NAME = "ID_TOKEN"
}

private fun userPreferencesMigrations(
    context: Context,
): List<DataMigration<Preferences>> = listOf(
    legacyDataStoreMigration(
        context = context,
        legacyStoreName = UserPreferencesStore.LEGACY_DATASTORE_NAME,
        keyMigrations = listOf(
            IntPreferencesKeyMigration(
                oldKeyName = UserPreferencesStore.USER_THEME_KEY_NAME,
                newKeyName = UserPreferencesStore.USER_THEME_KEY_NAME,
            ),
            StringPreferencesKeyMigration(
                oldKeyName = UserPreferencesStore.LEGACY_JWT_TOKEN_KEY_NAME,
                newKeyName = UserPreferencesStore.USER_JWT_TOKEN_KEY_NAME,
            ),
        ),
    ),
    sharedPreferencesMigration(
        context = context,
        legacySharedPreferencesName = UserPreferencesStore.LEGACY_SHARED_PREFERENCES_NAME,
        keyMigrations = listOf(
            SharedPreferencesIntKeyMigration(
                sharedPreferencesKeyName = UserPreferencesStore.USER_THEME_KEY_NAME,
                dataStoreKeyName = UserPreferencesStore.USER_THEME_KEY_NAME,
            ),
            SharedPreferencesStringKeyMigration(
                sharedPreferencesKeyName = UserPreferencesStore.LEGACY_JWT_TOKEN_KEY_NAME,
                dataStoreKeyName = UserPreferencesStore.USER_JWT_TOKEN_KEY_NAME,
            ),
        ),
    ),
    sharedPreferencesMigration(
        context = context,
        legacySharedPreferencesName = UserPreferencesStore.LEGACY_NANDA_SHARED_PREFERENCES_NAME,
        keyMigrations = listOf(
            SharedPreferencesStringKeyMigration(
                sharedPreferencesKeyName = UserPreferencesStore.LEGACY_NANDA_ID_TOKEN_KEY_NAME,
                dataStoreKeyName = UserPreferencesStore.USER_JWT_TOKEN_KEY_NAME,
            ),
        ),
    ),
)

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = UserPreferencesStore.STORE_NAME,
    produceMigrations = ::userPreferencesMigrations,
)

@Singleton
class ThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun getUserTheme() = context.userPreferencesDataStore.data.map { preferences ->
        preferences[USER_THEME]
    }

    suspend fun setUserTheme(theme: Int) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[USER_THEME] = theme
        }
    }

    private object PreferencesKeys {
        val USER_THEME = intPreferencesKey(UserPreferencesStore.USER_THEME_KEY_NAME)
    }
}
