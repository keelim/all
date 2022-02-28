package com.keelim.cnubus.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.keelim.cnubus.R
import com.keelim.cnubus.feature.map.ui.MapsActivity

/**
 * BroadCastReceiver
 */
class StationWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MapsActivity::class.java),
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )

        val views = RemoteViews(
            context?.packageName,
            R.layout.widget_main
        ).apply {
            setOnClickPendingIntent(R.id.layout_widget, pendingIntent)
        }
        appWidgetManager?.updateAppWidget(appWidgetIds, views)
    }
}