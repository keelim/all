package com.keelim.core.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.data.repository.StationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

val Context.stationDataStore: DataStore<Preferences> by preferencesDataStore(name = "station_preferences")

class StationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StationRepository {
    private val FAVORITE_STATIONS_KEY = stringSetPreferencesKey("favorite_stations")

    override val favoriteStations: Flow<Set<String>> = context.stationDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[FAVORITE_STATIONS_KEY] ?: emptySet()
        }

    override suspend fun addFavorite(stationName: String) {
        context.stationDataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_STATIONS_KEY] ?: emptySet()
            preferences[FAVORITE_STATIONS_KEY] = currentFavorites + stationName
        }
    }

    override suspend fun removeFavorite(stationName: String) {
        context.stationDataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_STATIONS_KEY] ?: emptySet()
            preferences[FAVORITE_STATIONS_KEY] = currentFavorites - stationName
        }
    }
}
