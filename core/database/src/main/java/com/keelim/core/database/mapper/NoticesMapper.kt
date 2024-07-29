package com.keelim.core.database.mapper

import com.keelim.core.database.model.NoticesEntity
import com.keelim.model.Notices

fun NoticesEntity.toNotices() = Notices(
    uid = uid,
    title = title,
    note = note,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun List<NoticesEntity>.toNotices() = map(NoticesEntity::toNotices)

fun Notices.toNoticesEntity() = NoticesEntity(
    uid = uid,
    title = title,
    note = note,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
