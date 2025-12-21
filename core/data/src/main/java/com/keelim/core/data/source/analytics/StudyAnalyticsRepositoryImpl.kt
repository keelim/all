package com.keelim.core.data.source.analytics

import com.keelim.core.database.mapper.toDailyStudyStats
import com.keelim.core.database.mapper.toSubjectStudyStats
import com.keelim.data.repository.StudyAnalyticsRepository
import com.keelim.model.DailyStudyStats
import com.keelim.model.SubjectStudyStats
import com.keelim.shared.data.database.dao.StudySessionDao
import com.keelim.shared.data.database.model.StudySession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import jakarta.inject.Inject

class StudyAnalyticsRepositoryImpl @Inject constructor(
    private val studySessionDao: StudySessionDao,
) : StudyAnalyticsRepository {

    override fun getDailyStats(): Flow<List<DailyStudyStats>> =
        studySessionDao.getDailyAggregated().map { it.toDailyStudyStats() }

    override fun getSubjectStats(): Flow<List<SubjectStudyStats>> =
        studySessionDao.getSubjectAggregated().map { it.toSubjectStudyStats() }

    override fun getTotalStudySeconds(): Flow<Int> =
        studySessionDao.getTotalStudySeconds().map { it ?: 0 }

    override fun getStudyDaysCount(): Flow<Int> =
        studySessionDao.getStudyDaysCount().map { it ?: 0 }

    override fun getCurrentStreak(): Flow<Int> =
        studySessionDao.getDailyAggregated().map { dailyStats ->
            calculateStreak(dailyStats.toDailyStudyStats())
        }

    override suspend fun recordSession(subject: String, durationSeconds: Int) {
        val session = StudySession(
            subject = subject,
            durationSeconds = durationSeconds,
        )
        studySessionDao.upsert(session)
    }

    private fun calculateStreak(dailyStats: List<DailyStudyStats>): Int {
        if (dailyStats.isEmpty()) return 0

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val studyDates = dailyStats.mapNotNull { stat ->
            runCatching { LocalDate.parse(stat.date) }.getOrNull()
        }.toSet()

        var streak = 0
        var checkDate = today

        while (studyDates.contains(checkDate)) {
            streak++
            checkDate = checkDate.minus(1, DateTimeUnit.DAY)
        }

        // If no study today, check if streak is from yesterday
        if (streak == 0 && studyDates.contains(today.minus(1, DateTimeUnit.DAY))) {
            checkDate = today.minus(1, DateTimeUnit.DAY)
            while (studyDates.contains(checkDate)) {
                streak++
                checkDate = checkDate.minus(1, DateTimeUnit.DAY)
            }
        }

        return streak
    }
}
