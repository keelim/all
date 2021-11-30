package com.keelim.cnubus.data.repository.station

import com.keelim.cnubus.data.db.entity.StationEntity
import com.keelim.cnubus.data.db.entity.SubwayEntity

interface StationApi {

    suspend fun getStationDataUpdatedTimeMillis(): Long

    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}
