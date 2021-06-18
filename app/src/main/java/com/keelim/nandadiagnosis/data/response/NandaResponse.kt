package com.keelim.nandadiagnosis.data.response

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2
import java.util.*

data class NandaResponse(
    val id: Int,
    val reason: String,
    val diagnosis: String,
    val domain_name: String,
    val class_name: String,
    val category: Int,
    val favorite: Int,
    val createdAt: Long,
) {
    fun toEntity(): NandaEntity2 =
        NandaEntity2(
            nanda_id = id,
            reason = reason,
            diagnosis = diagnosis,
            class_name = class_name,
            domain_name = domain_name,
            category = category,
            favorite = favorite,
            created_at = Date(createdAt)
        )
}

