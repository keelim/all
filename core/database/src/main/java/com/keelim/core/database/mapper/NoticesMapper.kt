package com.keelim.core.database.mapper

import com.keelim.model.Notices
import com.keelim.shared.data.database.model.NoticesEntity

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
