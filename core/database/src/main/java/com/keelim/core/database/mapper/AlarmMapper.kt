package com.keelim.core.database.mapper

import com.keelim.model.Alarm
import com.keelim.shared.data.database.model.AlarmEntity

fun AlarmEntity.toAlarm() = Alarm(
    title = title,
    subTitle = subTitle,
    receiveDate = receiveDate,
)

fun List<AlarmEntity>.toAlarm() = map(AlarmEntity::toAlarm)

fun Alarm.toAlarmEntity() = AlarmEntity(
    title = title,
    subTitle = subTitle,
    receiveDate = receiveDate,
)
