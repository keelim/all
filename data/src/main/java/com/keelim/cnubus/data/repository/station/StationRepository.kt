package com.keelim.cnubus.data.repository.station

import com.keelim.cnubus.data.model.ArrivalInformation
import com.keelim.cnubus.data.model.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    val stations: Flow<List<Station>>
    suspend fun refreshStations()

    suspend fun getStationArrivals(stationName: String): List<ArrivalInformation>

    suspend fun updateStation(station: Station)
}