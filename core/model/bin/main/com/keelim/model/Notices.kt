package com.keelim.model

data class Notices(
    val uid: Int = 0,
    val title: String,
    val note: String,
    val createdAt: String = System.currentTimeMillis().toString(),
    val updatedAt: String = System.currentTimeMillis().toString(),
)
