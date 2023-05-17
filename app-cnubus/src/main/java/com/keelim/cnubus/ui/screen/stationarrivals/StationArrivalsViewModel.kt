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
package com.keelim.cnubus.ui.screen.stationarrivals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.model.Station
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationArrivalsViewModel @Inject constructor() : ViewModel() {
    private val _station: MutableStateFlow<Station> =
        MutableStateFlow(Station("", false, emptyList()))
    val station: StateFlow<Station> = _station

    private val _state: MutableStateFlow<ArrivalState> = MutableStateFlow(ArrivalState.UnInitialized)
    val state: StateFlow<ArrivalState> = _state

    fun fetchStationArrivals() = viewModelScope.launch {
        try {
            _state.emit(ArrivalState.ShowLoading)
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
}
