package com.keelim.mygrade.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.keelim.mygrade.R
import com.keelim.mygrade.ui.center.CenterActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NotificationBuilderImpl @Inject constructor(
    @ApplicationContext ctx: Context
) : NotificationBuilder {

    private val applicationContext = ctx.applicationContext

    override fun showNotification(action: NotificationCompat.Action?) = applicationContext.run {
        NotificationSpecs.notifyLegacy(this) {
            setStyle(NotificationCompat.BigTextStyle())
            setSmallIcon(R.mipmap.ic_launcher_round)
            setContentTitle(buildSpannedString { bold { append("성적을 확인해보아요") } })
            setContentText("안녕하세요 마이그레이드 입니다.")
            setAutoCancel(true)
            action?.let{
                addAction(it)
            }
            setContentIntent(createLauncherIntent())
        }
    }

    private fun Context.createLauncherIntent(): PendingIntent {
        val intent = Intent(this, CenterActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }
}