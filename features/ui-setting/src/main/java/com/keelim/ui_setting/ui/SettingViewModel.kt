package com.keelim.ui_setting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.keelim.cnubus.domain.GetDevelopersUseCase
import com.keelim.common.toUiState
import com.keelim.compose.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getDevelopers: GetDevelopersUseCase
):ViewModel() {
    val developers = liveData {
        emit(UiState.loading())
        emit(getDevelopers().toUiState())
    }
}