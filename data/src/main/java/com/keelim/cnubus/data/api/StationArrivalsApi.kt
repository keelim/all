package com.keelim.cnubus.data.api

import com.keelim.cnubus.data.api.response.HouseDto
import com.keelim.cnubus.data.api.response.RealtimeStationArrivals
import com.keelim.cnubus.data.model.maps.House
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface StationArrivalsApi {
    @GET("api/subway/41645553676830303130316c78644a6d/json/realtimeStationArrival/0/100/{stationName}")
    suspend fun getRealtimeStationArrivals(@Path("stationName") stationName: String): Response<RealtimeStationArrivals>

    @GET
    suspend fun getLocationList(@Url url:String = "https://run.mocky.io/v3/511c37d3-79c1-455f-9efb-98b5d594e640"): Response<HouseDto>
}
