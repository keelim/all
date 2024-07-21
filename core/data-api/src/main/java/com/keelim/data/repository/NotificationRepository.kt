package com.keelim.data.repository

interface NotificationRepository {
    suspend fun getNotification(): List<Notification>
}
