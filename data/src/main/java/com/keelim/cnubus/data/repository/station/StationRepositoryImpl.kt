package com.keelim.cnubus.data.repository.station

import com.keelim.cnubus.data.api.StationArrivalsApi
import com.keelim.cnubus.data.api.response.HouseDto
import com.keelim.cnubus.data.api.response.mapper.toArrivalInformation
import com.keelim.cnubus.data.db.AppDatabase
import com.keelim.cnubus.data.db.SharedPreferenceManager
import com.keelim.cnubus.data.db.dao.StationDao
import com.keelim.cnubus.data.db.entity.mapper.toStationEntity
import com.keelim.cnubus.data.db.entity.mapper.toStations
import com.keelim.cnubus.data.model.ArrivalInformation
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StationRepositoryImpl @Inject constructor(
    private val stationArrivalsApi: StationArrivalsApi,
    private val stationApi: StationApi,
    private val db: AppDatabase,
    private val preferenceManager: SharedPreferenceManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): StationRepository {
    override val stations: Flow<List<Station>> =
        db.dao().getStationWithSubways()
            .distinctUntilChanged()
            .map { stations -> stations.toStations().sortedByDescending { it.isFavorited } }
            .flowOn(dispatcher)

    override suspend fun refreshStations() = withContext(dispatcher) {
        val fileUpdatedTimeMillis = stationApi.getStationDataUpdatedTimeMillis()
        val lastDatabaseUpdatedTimeMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        if (lastDatabaseUpdatedTimeMillis == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTimeMillis) {
            db.dao().insertStationSubways(stationApi.getStationSubways())
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, fileUpdatedTimeMillis)
        }
    }

    override suspend fun getStationArrivals(stationName: String): List<ArrivalInformation> = withContext(dispatcher) {
        stationArrivalsApi.getRealtimeStationArrivals(stationName)
            .body()
            ?.realtimeArrivalList
            ?.toArrivalInformation()
            ?.distinctBy { it.direction }
            ?.sortedBy { it.subway }
            ?: throw RuntimeException("도착 정보를 불러오는 데에 실패했습니다.")
    }

    override suspend fun updateStation(station: Station) = withContext(dispatcher) {
        db.dao().updateStation(station.toStationEntity())
    }

    override suspend fun getLocation(): HouseDto  = with(dispatcher){
        stationArrivalsApi.getLocationList().body() ?: throw RuntimeException("목적지를 불러오는 데에 실패했습니다.")
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
    }
}