package com.keelim.data.repository

import com.keelim.model.Notification

interface NotificationRepository {
    suspend fun getNotification(): List<Notification>
}
