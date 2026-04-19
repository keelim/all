package com.keelim.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KuiTheme

@Composable
fun KuiEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    val kuiColors = KuiTheme.colors
    val spacing = KuiTheme.spacing
    val colorScheme = KuiTheme.colorScheme
    val radius = KuiTheme.shapes.large
    val radiusSize = spacing.space6

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 160.dp)
            .dashedBorder(color = colorScheme.outline, cornerRadius = radiusSize)
            .padding(spacing.cardPadding),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            color = kuiColors.surfaceSoft,
            shape = radius,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.cardPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.space2),
            ) {
                Text(
                    text = title,
                    style = KuiTheme.typography.titleMedium,
                    color = colorScheme.onSurface,
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = KuiTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant,
                    )
                }
                action?.invoke()
            }
        }
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 1.5.dp,
    dashLength: Dp = 6.dp,
    gapLength: Dp = 4.dp,
    cornerRadius: Dp = 24.dp,
): Modifier = this.drawWithCache {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(dashLength.toPx(), gapLength.toPx()),
            phase = 0f,
        ),
    )
    onDrawBehind {
        drawRoundRect(
            color = color,
            style = stroke,
            cornerRadius = CornerRadius(cornerRadius.toPx()),
        )
    }
}
