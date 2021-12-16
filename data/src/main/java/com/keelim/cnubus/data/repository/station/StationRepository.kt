package com.keelim.cnubus.data.repository.station

import com.keelim.cnubus.data.api.response.HouseDto
import com.keelim.cnubus.data.model.ArrivalInformation
import com.keelim.cnubus.data.model.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    val stations: Flow<List<Station>>
    suspend fun refreshStations()

    suspend fun getStationArrivals(stationName: String): List<ArrivalInformation>

    suspend fun updateStation(station: Station)

    suspend fun getLocation(): HouseDto
}