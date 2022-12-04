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

import androidx.lifecycle.viewModelScope
import com.keelim.cnubus.data.repository.station.StationRepository
import com.keelim.common.base.BaseViewModel
import com.keelim.data.model.gps.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : BaseViewModel() {
    private val _state: MutableStateFlow<MapEvent> = MutableStateFlow(MapEvent.UnInitialized)
    val state: StateFlow<MapEvent> get() = _state
    private val data: MutableStateFlow<List<Location>> = MutableStateFlow(emptyList())

    fun loadLocation(mode: String) {
        stationRepository
            .locations
            .distinctUntilChanged()
            .map { locations ->
                when (mode) {
                    "a" -> locations.filter { it.roota != Location.EX_NUMBER }.sortedBy { it.roota }
                    "b" -> locations.filter { it.rootb != Location.EX_NUMBER }.sortedBy { it.rootb }
                    "c" -> locations.filter { it.rootc != Location.EX_NUMBER }.sortedBy { it.rootc }
                    else ->
                        locations.filter { it.rootc != Location.EX_NUMBER }
                            .sortedBy { it.rootc }
                }
            }
            .onStart {
                _state.emit(MapEvent.Loading)
            }.onEach { locations ->
                data.value = locations
                _state.emit(MapEvent.MigrateSuccess(locations))
            }
            .catch {
                it.printStackTrace()
                _state.emit(MapEvent.Error())
            }
            .launchIn(viewModelScope)
    }
}
