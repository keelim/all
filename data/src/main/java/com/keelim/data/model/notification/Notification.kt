package com.keelim.data.model.notification

import com.keelim.data.api.response.ResponseNotification

data class Notification(
    val version: String,
    val main: String,
    val desc: String,
)

fun ResponseNotification.toNotification(): List<Notification> {
    return this.values.map {
        Notification(
            version = it[0],
            main = it[1],
            desc = it[2],
        )
    }
}
