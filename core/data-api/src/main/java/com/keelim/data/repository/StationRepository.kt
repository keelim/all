package com.keelim.data.repository

import kotlinx.coroutines.flow.Flow

interface StationRepository {
    val favoriteStations: Flow<Set<String>>
    suspend fun addFavorite(stationName: String)
    suspend fun removeFavorite(stationName: String)
}
