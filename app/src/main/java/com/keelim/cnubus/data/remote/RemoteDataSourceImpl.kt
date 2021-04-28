package com.keelim.cnubus.data.remote

import com.keelim.cnubus.services.RoadService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val service:RoadService
): RemoteDataSource {
    override suspend fun getRoadInformation(type: String): List<String> {
        return  service.getRoadInformation(type)
    }
}