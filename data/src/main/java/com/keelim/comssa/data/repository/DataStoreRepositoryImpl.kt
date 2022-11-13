package com.keelim.comssa.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {
    override fun getValue(key: String, defaultValue: String): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[stringPreferencesKey(key)] ?: defaultValue
        }
    }

    override suspend fun setValue(key: String, value: String) {
        dataStore.edit { preference ->
            preference[stringPreferencesKey(key)] = value
        }
    }
}
