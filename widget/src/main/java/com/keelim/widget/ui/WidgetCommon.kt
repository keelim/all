package com.keelim.widget.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.text.Text
import androidx.glance.text.TextStyle


@Composable
fun WidgetCommon(
    title: String,
    style: TextStyle,
    modifier: GlanceModifier = GlanceModifier,
    context: Context = LocalContext.current,
) {
    Text(
        text = title,
        style = style,
        modifier = modifier.clickable {
            context.packageManager.getLaunchIntentForPackage(
                context.packageName
            )
        }
    )
}
