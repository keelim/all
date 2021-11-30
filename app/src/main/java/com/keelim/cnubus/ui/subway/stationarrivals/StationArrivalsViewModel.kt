package com.keelim.cnubus.ui.subway.stationarrivals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.data.repository.station.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class StationArrivalsViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {
    private val _station: MutableStateFlow<Station> =
        MutableStateFlow(Station("", false, emptyList()))
    val station: StateFlow<Station> = _station

    private val _state: MutableStateFlow<ArrivalState> = MutableStateFlow(ArrivalState.UnInitialized)
    val state: StateFlow<ArrivalState> = _state

    fun fetchStationArrivals() = viewModelScope.launch {
        try {
            _state.emit(ArrivalState.ShowLoading)
            _state.emit(ArrivalState.ShowStationArrivals(stationRepository.getStationArrivals(station.value.name)))
        } catch (exception: Exception) {
            exception.printStackTrace()
            _state.emit(ArrivalState.Error(exception.message ?: "알 수 없는 문제가 발생했어요 😢"))
        } finally {
            _state.emit(ArrivalState.HideLoading)
        }
    }


    fun setStation(value: Station) {
        _station.value = value
    }

    fun toggleStationFavorite() = viewModelScope.launch {
        stationRepository.updateStation(station.value.copy(isFavorited = !station.value.isFavorited))
    }
}
