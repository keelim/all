package com.keelim.core.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.core.data.source.local.StringSetPreferencesKeyMigration
import com.keelim.core.data.source.local.renameKeysInCurrentStoreMigration
import com.keelim.data.repository.StationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import java.io.IOException
import jakarta.inject.Inject

internal object StationStore {
    const val STORE_NAME = "station_preferences"
    const val LEGACY_FAVORITE_STATIONS_KEY_NAME = "favorite_stations"
    const val FAVORITE_STATIONS_KEY_NAME = "station_favorite_stations"
}

val Context.stationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = StationStore.STORE_NAME,
    produceMigrations = {
        listOf(
            renameKeysInCurrentStoreMigration(
                keyMigrations = listOf(
                    StringSetPreferencesKeyMigration(
                        oldKeyName = StationStore.LEGACY_FAVORITE_STATIONS_KEY_NAME,
                        newKeyName = StationStore.FAVORITE_STATIONS_KEY_NAME,
                    ),
                ),
            ),
        )
    },
)

class StationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StationRepository {
    private val favoriteStationsKey = stringSetPreferencesKey(StationStore.FAVORITE_STATIONS_KEY_NAME)

    override val favoriteStations: Flow<Set<String>> = context.stationDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[favoriteStationsKey] ?: emptySet()
        }

    override suspend fun addFavorite(stationName: String) {
        context.stationDataStore.edit { preferences ->
            val currentFavorites = preferences[favoriteStationsKey] ?: emptySet()
            preferences[favoriteStationsKey] = currentFavorites + stationName
        }
    }

    override suspend fun removeFavorite(stationName: String) {
        context.stationDataStore.edit { preferences ->
            val currentFavorites = preferences[favoriteStationsKey] ?: emptySet()
            preferences[favoriteStationsKey] = currentFavorites - stationName
        }
    }
}
