package com.keelim.mygrade.ui.screen.timer.history

import androidx.lifecycle.ViewModel
import com.keelim.data.source.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TimerHistoryViewModel @Inject constructor(
    val historyRepository: HistoryRepository
): ViewModel()
