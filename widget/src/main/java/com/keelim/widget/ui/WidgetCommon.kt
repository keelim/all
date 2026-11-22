package com.keelim.widget.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

@Composable
fun WidgetCommon(
    title: String,
    style: TextStyle,
    modifier: GlanceModifier = GlanceModifier,
    context: Context = LocalContext.current,
) {
    val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    val clickModifier = if (launchIntent != null) {
        modifier.clickable(actionStartActivity(launchIntent))
    } else {
        modifier
    }
    Text(
        text = title,
        style = style,
        modifier = clickModifier,
    )
}
