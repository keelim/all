package com.keelim.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.domain.video.VideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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