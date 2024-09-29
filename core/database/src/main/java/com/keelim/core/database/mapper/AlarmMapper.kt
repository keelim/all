package com.keelim.core.database.mapper

import com.keelim.core.database.model.AlarmEntity
import com.keelim.model.Alarm

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
