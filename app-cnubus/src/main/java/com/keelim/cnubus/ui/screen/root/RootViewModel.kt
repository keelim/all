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
package com.keelim.cnubus.ui.screen.root

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.core.data.model.Location
import com.keelim.core.data.model.locationList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.keelim.data.repository.StationRepository
import jakarta.inject.Inject
@Stable
@HiltViewModel
class RootViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {
    private val modes = MutableStateFlow("a")
    private val _query = MutableStateFlow("")
    private val retrySignal = MutableStateFlow(0)
    val query: StateFlow<String> = _query.asStateFlow()

    val state: StateFlow<MapEvent> = retrySignal
        .flatMapLatest { retryCount ->
            val events: Flow<MapEvent> = combine(
                modes,
                _query,
                stationRepository.favoriteStations,
            ) { mode, query, favorites ->
                val locations = when (mode) {
                    "a" -> locationList.filter { it.roota != 999 }.sortedBy { it.roota }
                    "b" -> locationList.filter { it.rootb != 999 }.sortedBy { it.rootb }
                    "c" -> locationList.filter { it.rootc != 999 }.sortedBy { it.rootc }
                    "d" -> locationList.filter { it.root_night != 999 }.sortedBy { it.root_night }
                    "f" -> locationList.filter { favorites.contains(it.name) }
                    "s" -> if (query.isEmpty()) emptyList() else locationList.filter { it.name.contains(query) }
                    else -> emptyList()
                }
                MapEvent.MigrateSuccess(locations)
            }

            val retryAwareEvents = if (retryCount > 0) {
                events.onStart { emit(MapEvent.Loading) }
            } else {
                events
            }

            retryAwareEvents
                .onEach { event ->
                    if (event is MapEvent.MigrateSuccess) {
                        _data.tryEmit(event.data)
                    }
                }
                .catch { emit(MapEvent.Error()) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), MapEvent.UnInitialized)

    private val _data = MutableStateFlow<List<Location>>(emptyList())
    val data: StateFlow<List<Location>> = _data.asStateFlow()

    val favorites: StateFlow<Set<String>> = stationRepository.favoriteStations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptySet())

    fun setMode(mode: String) {
        modes.tryEmit(mode)
    }

    fun updateQuery(newQuery: String) {
        _query.tryEmit(newQuery)
    }

    fun retry() {
        retrySignal.update { it + 1 }
    }

    fun toggleFavorite(stationName: String) {
        viewModelScope.launch {
            val currentFavorites = favorites.value
            if (currentFavorites.contains(stationName)) {
                stationRepository.removeFavorite(stationName)
            } else {
                stationRepository.addFavorite(stationName)
            }
        }
    }
}
