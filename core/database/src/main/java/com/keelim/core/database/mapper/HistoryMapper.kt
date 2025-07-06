package com.keelim.core.database.mapper

import com.keelim.shared.data.database.model.SimpleHistory

fun List<SimpleHistory>.toSimpleHistoryModel(): List<com.keelim.model.SimpleHistory> {
    return map { history ->
        com.keelim.model.SimpleHistory(
            subject = history.subject,
            date = history.date,
            grade = history.grade,
            gradeRank = history.gradeRank,
            totalRank = history.totalRank,
        )
    }
}
