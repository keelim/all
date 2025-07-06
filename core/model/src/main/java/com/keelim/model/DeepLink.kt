package com.keelim.model

data class DeepLink(
    val url: String,
    val timestamp: Long,
    val isBookMarked: Boolean = false,
    val title: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val usageCount: Int = 0,
    val lastUsed: Long = 0L,
) {
    companion object {
        val EMPTY = DeepLink(
            url = "",
            timestamp = 0L,
            isBookMarked = false,
            title = "",
            imageUrl = "",
            category = "",
            usageCount = 0,
            lastUsed = 0L,
        )
    }
}

data class UsageStat(
    val day: String,
    val count: Int
)
