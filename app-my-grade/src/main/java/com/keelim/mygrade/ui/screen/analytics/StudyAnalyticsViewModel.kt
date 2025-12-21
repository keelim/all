package com.keelim.mygrade.ui.screen.analytics

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.StudyAnalyticsRepository
import com.keelim.model.DailyStudyStats
import com.keelim.model.SubjectStudyStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import jakarta.inject.Inject

data class StudyAnalyticsUiState(
    val dailyStats: List<DailyStudyStats> = emptyList(),
    val subjectStats: List<SubjectStudyStats> = emptyList(),
    val totalSeconds: Int = 0,
    val studyDaysCount: Int = 0,
    val currentStreak: Int = 0,
    val isLoading: Boolean = true,
)

@Stable
@HiltViewModel
class StudyAnalyticsViewModel @Inject constructor(
    private val studyAnalyticsRepository: StudyAnalyticsRepository,
) : ViewModel() {

    val uiState: StateFlow<StudyAnalyticsUiState> = combine(
        studyAnalyticsRepository.getDailyStats(),
        studyAnalyticsRepository.getSubjectStats(),
        studyAnalyticsRepository.getTotalStudySeconds(),
        studyAnalyticsRepository.getStudyDaysCount(),
        studyAnalyticsRepository.getCurrentStreak(),
    ) { dailyStats, subjectStats, totalSeconds, studyDaysCount, currentStreak ->
        StudyAnalyticsUiState(
            dailyStats = dailyStats,
            subjectStats = subjectStats,
            totalSeconds = totalSeconds,
            studyDaysCount = studyDaysCount,
            currentStreak = currentStreak,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StudyAnalyticsUiState(),
    )

    fun recordSession(subject: String, durationSeconds: Int) {
        viewModelScope.launch {
            studyAnalyticsRepository.recordSession(subject, durationSeconds)
        }
    }
}
