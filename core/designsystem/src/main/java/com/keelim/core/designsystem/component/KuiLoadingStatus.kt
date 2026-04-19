package com.keelim.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KuiTheme

enum class KuiLoadingVariant { Inline, Panel }

@Composable
fun KuiLoadingStatus(
    modifier: Modifier = Modifier,
    variant: KuiLoadingVariant = KuiLoadingVariant.Inline,
    label: String = "로딩 중…",
    steps: List<String> = emptyList(),
    activeStep: Int = 0,
) {
    when (variant) {
        KuiLoadingVariant.Inline -> InlineLoading(modifier, label)
        KuiLoadingVariant.Panel  -> PanelLoading(modifier, label, steps, activeStep)
    }
}

@Composable
private fun InlineLoading(modifier: Modifier, label: String) {
    val spacing = KuiTheme.spacing

    Row(
        modifier = modifier.padding(horizontal = spacing.space4, vertical = spacing.space2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.space2),
    ) {
        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
        Text(
            text = label,
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun PanelLoading(
    modifier: Modifier,
    label: String,
    steps: List<String>,
    activeStep: Int,
) {
    val kuiColors = KuiTheme.colors
    val spacing = KuiTheme.spacing
    val colorScheme = KuiTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = KuiTheme.shapes.large,
        color = kuiColors.surfaceSoft,
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(spacing.space3))
                Text(
                    text = label,
                    style = KuiTheme.typography.titleMedium,
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            if (steps.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing.space3),
                    horizontalArrangement = Arrangement.spacedBy(spacing.space2),
                ) {
                    itemsIndexed(steps) { index, step ->
                        val active = index == activeStep
                        Text(
                            text = step,
                            style = KuiTheme.typography.labelMedium,
                            color = if (active)
                                colorScheme.onSurface
                            else
                                colorScheme.onSurfaceVariant,
                            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}
