package com.keelim.cnubus.data.api.response


import com.google.gson.annotations.SerializedName
import com.keelim.cnubus.data.api.response.ErrorMessage
import com.keelim.cnubus.data.api.response.RealtimeArrival

data class RealtimeStationArrivals(
    @SerializedName("errorMessage")
    val errorMessage: ErrorMessage? = null,
    @SerializedName("realtimeArrivalList")
    val realtimeArrivalList: List<RealtimeArrival>? = null
)
