package com.keelim.model

data class DeepLink(
    val url: String,
    val timestamp: Long,
    val isBookMarked: Boolean = false,
    val title: String = "",
    val imageUrl: String = "",
)
