package com.keelim.nandadiagnosis.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat.Builder

object NotificationSpecs {
    const val TYPE_NOTICE = "Notice"

    inline fun notifyAsNotice(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(1, Builder(ctx, NotificationChannels.NOTICE).intercept().build())
        }
    }
}

fun Context.getNotificationManager(): NotificationManager? {
    return getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
}