package com.keelim.setting.screen.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.data.repository.AlarmRepository
import com.keelim.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject




@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    val screenState: StateFlow<SealedUiState<List<Alarm>>> = alarmRepository
        .getAlarms()
        .asSealedUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), SealedUiState.Loading)
}
