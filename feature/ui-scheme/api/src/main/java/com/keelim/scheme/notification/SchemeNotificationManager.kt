package com.keelim.scheme.notification

interface SchemeNotificationManager {
    fun showDeepLinkNotification(
        notificationId: Int,
        title: String,
        message: String,
        deepLinkUri: String,
    )

    companion object {
        const val CHANNEL_ID = "deep_link_channel"
        const val CHANNEL_NAME = "Deep Link Notifications"
        const val CHANNEL_DESCRIPTION = "Notifications for deep linking into the app"
    }
}
