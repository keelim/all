package com.keelim.core.database.mapper

import com.keelim.model.DailyStudyStats
import com.keelim.model.SubjectStudyStats
import com.keelim.shared.data.database.model.StudySession

/** Convert list of StudySessions to aggregated daily stats */
fun List<StudySession>.toDailyStudyStats(): List<DailyStudyStats> =
    groupBy { it.date }
        .map { (date, sessions) ->
            DailyStudyStats(
                date = date,
                totalSeconds = sessions.sumOf { it.durationSeconds },
            )
        }
        .sortedByDescending { it.date }

/** Convert list of StudySessions to aggregated subject stats */
fun List<StudySession>.toSubjectStudyStats(): List<SubjectStudyStats> =
    groupBy { it.subject }
        .map { (subject, sessions) ->
            SubjectStudyStats(
                subject = subject.ifEmpty { "General" },
                totalSeconds = sessions.sumOf { it.durationSeconds },
            )
        }
        .sortedByDescending { it.totalSeconds }


