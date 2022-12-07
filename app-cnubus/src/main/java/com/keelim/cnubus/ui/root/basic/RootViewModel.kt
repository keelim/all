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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.model.gps.Location
import com.keelim.map.MapEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RootViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow<MapEvent>(MapEvent.UnInitialized)
    val state: StateFlow<MapEvent> = _state.asStateFlow()
    private val _data = MutableStateFlow<List<Location>>(emptyList())
    val data: StateFlow<List<Location>> = _data.asStateFlow()
    val modes = MutableStateFlow("")

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
    }
}
