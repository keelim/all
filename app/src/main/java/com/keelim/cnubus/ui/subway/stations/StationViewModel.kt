package com.keelim.cnubus.ui.subway.stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.data.repository.station.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class StationViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {

    private val queryString: MutableStateFlow<String> = MutableStateFlow("")
    private val stations: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList())

    private val _state: MutableStateFlow<StationState> = MutableStateFlow(StationState.UnInitialized)
    val state: StateFlow<StationState> = _state

    init {
        observeStations()
    }

    fun onViewCreated() = viewModelScope.launch {
        _state.emit(StationState.ShowStation(stations.value))
        stationRepository.refreshStations()
    }

    fun filterStations(query: String) = viewModelScope.launch {
        queryString.emit(query)
    }

    private fun observeStations() {
        stationRepository
            .stations
            .combine(queryString) { stations, query ->
                if (query.isBlank()) {
                    stations
                } else {
                    stations.filter { it.name.contains(query) }
                }
            }
            .onStart {
                _state.emit(StationState.ShowLoading)
            }
            .onEach {
                if (it.isNotEmpty()) {
                    _state.emit(StationState.HideLoading)
                }
                stations.value = it
                _state.emit(StationState.ShowStation(it))
            }
            .catch {
                it.printStackTrace()
                _state.emit(StationState.HideLoading)
            }
            .launchIn(viewModelScope)
    }

    fun toggleStationFavorite(station: Station) = viewModelScope.launch {
        stationRepository.updateStation(station.copy(isFavorited = !station.isFavorited))
    }
}
