package com.keelim.model

data class Notification(
    val date: String,
    val title: String,
    val desc: String,
    val fixed: Boolean,
    val faq: Boolean,
)
