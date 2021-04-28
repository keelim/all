package com.keelim.cnubus.services

import retrofit2.http.GET
import retrofit2.http.Path

interface RoadService {
    @GET("api/v1/road_information/{root}")
    suspend fun getRoadInformation(@Path("root") type: String): List<String>
}