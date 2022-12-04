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
package com.keelim.cnubus.ui.root.basic

import androidx.lifecycle.viewModelScope
import com.keelim.cnubus.data.db.entity.History
import com.keelim.cnubus.data.model.gps.Location
import com.keelim.cnubus.data.repository.station.StationRepository
import com.keelim.cnubus.domain.UserUseCase
import com.keelim.cnubus.feature.map.ui.MapEvent
import com.keelim.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    private val userUseCase: UserUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow<MapEvent>(MapEvent.UnInitialized)
    val state: StateFlow<MapEvent> = _state.asStateFlow()
    private val _data = MutableStateFlow<List<Location>>(emptyList())
    val data: StateFlow<List<Location>> = _data.asStateFlow()
    val modes = MutableStateFlow("")

    init { observeLocation() }

    private fun observeLocation() {
        stationRepository
            .locations
            .distinctUntilChanged()
            .combine(modes) { locations, mode ->
                when (mode) {
                    "a" -> locations.filter { it.roota != Location.EX_NUMBER }.sortedBy { it.roota }
                    "b" -> locations.filter { it.rootb != Location.EX_NUMBER }.sortedBy { it.rootb }
                    "c" -> locations.filter { it.rootc != Location.EX_NUMBER }.sortedBy { it.rootc }
                    "night" -> locations.filter { it.root_night != Location.EX_NUMBER }.sortedBy { it.root_night }
                    else -> locations.filter { it.rootc != Location.EX_NUMBER }.sortedBy { it.rootc }
                }
            }
            .onStart {
                _state.emit(MapEvent.Loading)
            }.onEach { locations ->
                _data.value = locations
                _state.emit(MapEvent.MigrateSuccess(locations))
            }
            .catch {
                it.printStackTrace()
                _state.emit(MapEvent.Error())
            }
            .launchIn(viewModelScope)
    }

    fun rootChange(root: String) = viewModelScope.launch {
        when (root) {
            "a" -> modes.emit("a")
            "b" -> modes.emit("b")
            "c" -> modes.emit("c")
            "night" -> modes.emit("night")
        }
    }

    fun insertHistory(position: Int, mode: String?) = viewModelScope.launch {
        val location = data.value.getOrNull(position) ?: Location.defaultLocation()
        userUseCase.insertHistory(
            History(
                destination = location.name,
                root = mode ?: "Empty"
            )
        )
    }
}
