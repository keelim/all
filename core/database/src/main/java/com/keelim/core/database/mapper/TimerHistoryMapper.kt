package com.keelim.core.database.mapper

import com.keelim.model.TimerHistoryModel
import com.keelim.shared.data.database.model.TimerHistory

fun TimerHistory.toModel(): TimerHistoryModel = TimerHistoryModel(
    uid = uid,
    date = date,
    hours = hours,
    minutes = minutes,
    seconds = seconds,
    description = description,
    isCompleted = isCompleted,
)

fun List<TimerHistory>.toTimerHistoryModels(): List<TimerHistoryModel> = map { it.toModel() }
