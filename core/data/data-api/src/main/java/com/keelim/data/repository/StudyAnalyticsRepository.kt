package com.keelim.data.repository

import com.keelim.model.DailyStudyStats
import com.keelim.model.SubjectStudyStats
import kotlinx.coroutines.flow.Flow

interface StudyAnalyticsRepository {
    fun getDailyStats(): Flow<List<DailyStudyStats>>
    fun getSubjectStats(): Flow<List<SubjectStudyStats>>
    fun getTotalStudySeconds(): Flow<Int>
    fun getStudyDaysCount(): Flow<Int>
    fun getCurrentStreak(): Flow<Int>
    suspend fun recordSession(subject: String, durationSeconds: Int)
}
