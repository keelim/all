package com.keelim.cnubus.ui.subway.stations

import com.keelim.cnubus.data.model.Station

sealed class StationState {
    object UnInitialized: StationState()
    object ShowLoading : StationState()
    object HideLoading : StationState()
    data class ShowStation(
        val data: List<Station>,
    ) : StationState()
}
