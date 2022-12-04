package com.keelim.data.model.notification.mapepr

import com.keelim.data.api.response.ResponseNotification
import com.keelim.data.model.notification.Notification

fun ResponseNotification.toNotification(): List<Notification> {
    return this.values.map {
        Notification(
            version = it[0],
            main = it[1],
            desc = it[2],
        )
    }
}