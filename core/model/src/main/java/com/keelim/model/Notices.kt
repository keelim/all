package com.keelim.model

import java.util.Date

data class Notices(
    val uid: Int = 0,
    val title: String,
    val note: String,
    val createdAt: Date = Date(System.currentTimeMillis()),
    val updatedAt: Date = Date(System.currentTimeMillis()),
)
