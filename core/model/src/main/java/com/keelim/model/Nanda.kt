package com.keelim.model

data class NandasResponse(
    val items: List<NandaResponse>,
    val count: Int,
)

data class NandaResponse(
    val id: Int,
    val reason: String,
    val diagnosis: String,
    val domain_name: String,
    val class_name: String,
    val category: Int,
    val favorite: Int,
    val createdAt: Long,
)
