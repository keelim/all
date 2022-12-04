/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui.stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.data.repository.station.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {

    private val queryString: MutableStateFlow<String> = MutableStateFlow("")
    private val stations: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList())

    private val _state: MutableStateFlow<StationState> =
        MutableStateFlow(StationState.UnInitialized)
    val state: StateFlow<StationState>
        get() = _state

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
