package com.keelim.data.model.notification.mapepr

import com.keelim.data.model.notification.Notification
import com.keelim.data.response.ResponseNotification

fun ResponseNotification.toNotification(): List<Notification> {
    return this.values.map {
        Notification(
            version = it[0],
            main = it[1],
            desc = it[2],
        )
    }
}
