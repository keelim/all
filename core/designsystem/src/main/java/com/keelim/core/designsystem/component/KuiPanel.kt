package com.keelim.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keelim.core.designsystem.theme.KuiTheme

enum class KuiPanelVariant { Default, Elevated, Soft }

@Composable
fun KuiPanel(
    modifier: Modifier = Modifier,
    variant: KuiPanelVariant = KuiPanelVariant.Default,
    content: @Composable ColumnScope.() -> Unit,
) {
    val kuiColors = KuiTheme.colors
    val spacing = KuiTheme.spacing
    val shape = KuiTheme.shapes.large

    when (variant) {
        KuiPanelVariant.Default -> Surface(
            modifier = modifier,
            shape = shape,
            color = kuiColors.surfaceSoft,
            tonalElevation = KuiTheme.elevation.none,
        ) {
            Column(modifier = Modifier.padding(spacing.cardPadding), content = content)
        }

        KuiPanelVariant.Elevated -> ElevatedCard(
            modifier = modifier,
            shape = shape,
        ) {
            Column(modifier = Modifier.padding(spacing.cardPadding), content = content)
        }

        KuiPanelVariant.Soft -> Surface(
            modifier = modifier,
            shape = shape,
            color = kuiColors.surfaceSoft,
            tonalElevation = KuiTheme.elevation.none,
        ) {
            Column(modifier = Modifier.padding(spacing.cardPadding), content = content)
        }
    }
}
