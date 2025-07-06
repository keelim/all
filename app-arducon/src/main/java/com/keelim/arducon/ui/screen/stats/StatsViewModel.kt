package com.keelim.arducon.ui.screen.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.ArduconRepository
import com.keelim.model.DeepLink
import com.keelim.model.UsageStat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    repository: ArduconRepository
) : ViewModel() {
    val topUsedLinks: StateFlow<List<DeepLink>> = repository.getTopUsedLinks(limit = 5)
        .stateIn(viewModelScope, started = WhileSubscribed(5_000L), emptyList())
    val recentUsedLinks: StateFlow<List<DeepLink>> = repository.getRecentUsedLinks(limit = 10)
        .stateIn(viewModelScope, started = WhileSubscribed(5_000L), emptyList())
    val dailyUsageStats: StateFlow<List<UsageStat>> = repository.getDailyUsageStats(limit = 7)
        .stateIn(viewModelScope, started = WhileSubscribed(5_000L), emptyList())
}
