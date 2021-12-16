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
import com.keelim.cnubus.data.model.MapEvent
import com.keelim.cnubus.data.model.gps.locationList
import com.keelim.cnubus.data.repository.station.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {
    val data = locationList
    private val _state: MutableStateFlow<MapEvent> = MutableStateFlow(MapEvent.UnInitialized)
    val state: StateFlow<MapEvent> get() = _state

    init {
        getLocation()
    }

    fun getLocation() = viewModelScope.launch {
        _state.emit(MapEvent.Loading)
        runCatching {
            stationRepository.getLocation()
        }.onSuccess {
            _state.emit(MapEvent.Success(it))
        }.onFailure {
            _state.emit(MapEvent.Error("오류가 발생하였습니다."))
        }
    }
}
