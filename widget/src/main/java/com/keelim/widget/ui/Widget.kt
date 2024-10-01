package com.keelim.widget.ui

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.TextDefaults

class Widget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            LazyColumn(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(GlanceTheme.colors.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    WidgetCommon(
                        title = context.packageName,
                        style = TextDefaults.defaultTextStyle.copy(
                            fontSize = 24.sp,
                            color = GlanceTheme.colors.onSurface
                        ),
                        context = context,
                    )
                }
                item {
                    Spacer(
                        modifier = GlanceModifier.height(16.dp),
                    )
                }
                item {
                    WidgetCommon(
                        title = "현재 준비 중입니다. ",
                        style = TextDefaults.defaultTextStyle.copy(
                            fontSize = 16.sp,
                            color = GlanceTheme.colors.onSurface
                        ),
                        context = context,
                    )
                }
            }
        }
    }
}
