package com.keelim.core.data.source.notification

data class Notification(
    val date: String,
    val title: String,
    val desc: String,
    val fixed: Boolean,
)

interface NotificationRepository {
    suspend fun getNotification(): List<Notification>
}
