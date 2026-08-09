package com.keelim.core.database.mapper

import com.keelim.model.UsageStat
import com.keelim.shared.data.database.model.UsageStatEntity

fun UsageStatEntity.toUsageStat() = UsageStat(
    day = day,
    count = count,
)

fun List<UsageStatEntity>.toUsageStat() = map(UsageStatEntity::toUsageStat)
