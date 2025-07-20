package com.keelim.core.database.mapper

import com.keelim.model.LengthRecord
import com.keelim.shared.data.database.model.LengthRecord as EntityLengthRecord

fun EntityLengthRecord.toDomain(): LengthRecord =
    LengthRecord(
        date = this.date,
        length = this.length
    )

fun LengthRecord.toEntity(): EntityLengthRecord =
    EntityLengthRecord(
        date = this.date,
        length = this.length
    )
