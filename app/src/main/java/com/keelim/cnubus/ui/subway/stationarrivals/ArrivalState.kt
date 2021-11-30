package com.keelim.cnubus.ui.subway.stationarrivals

import com.keelim.cnubus.data.model.ArrivalInformation

sealed class ArrivalState {
    object UnInitialized: ArrivalState()
    object ShowLoading : ArrivalState()
    object HideLoading : ArrivalState()
    data class ShowStationArrivals(
        val data: List<ArrivalInformation>,
    ) : ArrivalState()
    data class Error(val message:String): ArrivalState()
}
