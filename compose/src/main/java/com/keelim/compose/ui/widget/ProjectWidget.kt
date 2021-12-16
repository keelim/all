package com.keelim.compose.ui.widget

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class ProjectWidget: GlanceAppWidget() {
    @Composable
    override fun Content() {
        WidgetContent()
    }
}

class ProjectGlanceWidgetReceiver: GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget
        get() = ProjectWidget()
}