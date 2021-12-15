/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.domain.video.VideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
  private val videoUseCase: VideoUseCase
) : ViewModel() {
  private val _state: MutableStateFlow<PlayState> = MutableStateFlow(PlayState.UnInitialized)
  val state: StateFlow<PlayState> get() = _state

  fun getVideo() = viewModelScope.launch {
    _state.emit(PlayState.Loading)
    runCatching {
      videoUseCase.getVideoList()
    }.onSuccess {
      _state.emit(PlayState.Success(it.videos))
    }.onFailure {
      _state.emit(PlayState.Error("비디오를 불러오지 못했습닌다. 다시 한번 시도해주세요"))
    }
  }
}
