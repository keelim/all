package com.keelim.cnubus.data.model

import com.keelim.cnubus.data.api.response.HouseDto

sealed class MapEvent{
    object UnInitialized: MapEvent()
    object Loading: MapEvent()
    data class Success(val data:HouseDto): MapEvent()
    data class Error(val message:String): MapEvent()
}
