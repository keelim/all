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
package com.keelim.cnubus.feature.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.data.model.gps.Location
import com.keelim.cnubus.data.model.gps.locationList
import com.keelim.cnubus.data.repository.station.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<MapEvent> = MutableStateFlow(MapEvent.UnInitialized)
    val state: StateFlow<MapEvent> get() = _state
    private val data: MutableStateFlow<List<Location>> = MutableStateFlow(emptyList())

    init {
        observeLocation()
    }

    private fun observeLocation(){
        stationRepository.locations
            .onStart {
                _state.emit(MapEvent.Loading)
            }.onEach {
                data.value = it
                _state.emit(MapEvent.MigrateSuccess(it))
            }
            .catch {
                it.printStackTrace()
                _state.emit(MapEvent.Error())
            }
            .launchIn(viewModelScope)
    }
}
