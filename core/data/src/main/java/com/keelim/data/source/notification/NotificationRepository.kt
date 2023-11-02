package com.keelim.data.source.notification

data class Notification(
    val date: String,
    val title: String,
    val desc: String,
)

interface NotificationRepository {
    suspend fun getNotification(): List<Notification>
}
