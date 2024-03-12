package com.keelim.core.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.keelim.core.data.source.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN]
        }
    }

    suspend fun setToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun deleteToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN)
        }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("jwt_token")
    }
}
