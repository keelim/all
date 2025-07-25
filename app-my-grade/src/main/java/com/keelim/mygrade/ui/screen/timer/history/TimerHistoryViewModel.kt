package com.keelim.mygrade.ui.screen.timer.history

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.keelim.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@Stable
@HiltViewModel
class TimerHistoryViewModel @Inject constructor(
    val historyRepository: HistoryRepository,
) : ViewModel()
