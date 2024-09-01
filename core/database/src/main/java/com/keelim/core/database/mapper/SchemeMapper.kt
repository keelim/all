package com.keelim.core.database.mapper

import com.keelim.core.database.model.SchemeEntity

fun SchemeEntity.toPlain() = url

fun List<SchemeEntity>.toPlain() = map(SchemeEntity::toPlain)

fun String.toSchemeEntity() = SchemeEntity(
    url = this,
    timestamp = System.currentTimeMillis(),
)
