package com.keelim.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults


@Composable
fun WidgetCommon(
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current

    Text(
        text = "all project",
        style = TextDefaults.defaultTextStyle.copy(
            fontSize = 24.sp,
            color = GlanceTheme.colors.onSurface
        ),
        modifier = modifier.clickable {
            context.packageManager.getLaunchIntentForPackage(
                context.packageName
            )
        }
    )
}
