package com.keelim.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KuiTheme

enum class KuiTone { Default, Accent, Muted, Success, Warning, Info, Danger }

data class KuiTabItem(
    val label: String,
    val enabled: Boolean = true,
)

@Composable
fun KuiCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    elevated: Boolean = false,
    padded: Boolean = true,
    shape: Shape = KuiTheme.shapes.large,
    colors: CardColors = CardDefaults.cardColors(containerColor = KuiTheme.colorScheme.surface),
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = if (elevated) KuiTheme.elevation.card else KuiTheme.elevation.none,
    ),
    border: BorderStroke? = if (padded) BorderStroke(1.dp, KuiTheme.colorScheme.outline) else null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val spacing = KuiTheme.spacing
    val cardContent: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = if (padded) Modifier.padding(spacing.cardPadding) else Modifier,
            verticalArrangement = Arrangement.spacedBy(spacing.space2),
            content = content,
        )
    }
    if (onClick == null) {
        Card(
            modifier = modifier,
            shape = shape,
            border = border,
            colors = colors,
            elevation = elevation,
            content = cardContent,
        )
    } else {
        Card(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            border = border,
            colors = colors,
            elevation = elevation,
            interactionSource = interactionSource,
            content = cardContent,
        )
    }
}

@Composable
fun KuiAlert(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    tone: KuiTone = KuiTone.Default,
    leading: (@Composable () -> Unit)? = null,
) {
    val spacing = KuiTheme.spacing
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = KuiTheme.shapes.medium,
        color = kuiContainerColor(tone),
        border = BorderStroke(1.dp, kuiBorderColor(tone)),
    ) {
        Row(
            modifier = Modifier.padding(spacing.space4),
            horizontalArrangement = Arrangement.spacedBy(spacing.space3),
            verticalAlignment = Alignment.Top,
        ) {
            leading?.invoke()
            Column(verticalArrangement = Arrangement.spacedBy(spacing.space1)) {
                Text(
                    text = title,
                    style = KuiTheme.typography.titleMedium,
                    color = kuiContentColor(tone),
                    fontWeight = FontWeight.SemiBold,
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = KuiTheme.typography.bodyMedium,
                        color = kuiContentColor(tone),
                    )
                }
            }
        }
    }
}

@Composable
fun KuiMetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    delta: String? = null,
    tone: KuiTone = KuiTone.Default,
) {
    KuiCard(modifier = modifier, elevated = true) {
        Text(
            text = label,
            style = KuiTheme.type.overline,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = KuiTheme.type.ticker,
            color = KuiTheme.colorScheme.onSurface,
        )
        if (delta != null) {
            KuiBadge(text = delta, tone = tone.toBadgeTone())
        }
    }
}

@Composable
fun KuiDataNumber(
    value: String,
    modifier: Modifier = Modifier,
    tone: KuiTone = KuiTone.Default,
) {
    Text(
        text = value,
        modifier = modifier,
        style = KuiTheme.type.numeric,
        color = kuiContentColor(tone),
    )
}

@Composable
fun KuiCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val spacing = KuiTheme.spacing
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
        Text(
            text = label,
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun KuiRadioRow(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val spacing = KuiTheme.spacing
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onClick, enabled = enabled)
        Text(
            text = label,
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun KuiSwitchRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val spacing = KuiTheme.spacing
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
    }
}

@Composable
fun KuiProgress(
    modifier: Modifier = Modifier,
    value: Float? = null,
    tone: KuiTone = KuiTone.Accent,
) {
    if (value == null) {
        LinearProgressIndicator(
            modifier = modifier.fillMaxWidth(),
            color = kuiContentColor(tone),
            trackColor = KuiTheme.colors.surfaceStrong,
        )
    } else {
        LinearProgressIndicator(
            progress = { value.coerceIn(0f, 1f) },
            modifier = modifier.fillMaxWidth(),
            color = kuiContentColor(tone),
            trackColor = KuiTheme.colors.surfaceStrong,
        )
    }
}

@Composable
fun KuiSkeleton(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 16.dp,
) {
    val sized = when (width) {
        null -> modifier.fillMaxWidth()
        else -> modifier.width(width)
    }
    Box(
        modifier = sized
            .height(height)
            .background(KuiTheme.colors.surfaceStrong, KuiTheme.shapes.small),
    )
}

@Composable
fun KuiTabs(
    tabs: List<KuiTabItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tabs.isEmpty()) return

    val safeIndex = selectedIndex.coerceIn(0, tabs.lastIndex)
    PrimaryTabRow(
        selectedTabIndex = safeIndex,
        modifier = modifier,
        containerColor = KuiTheme.colorScheme.surface,
        contentColor = KuiTheme.colorScheme.primary,
    ) {
        tabs.forEachIndexed { index, item ->
            Tab(
                selected = index == safeIndex,
                onClick = { onSelected(index) },
                enabled = item.enabled,
                text = {
                    Text(
                        text = item.label,
                        style = KuiTheme.typography.labelLarge,
                        color = if (index == safeIndex) {
                            KuiTheme.colorScheme.primary
                        } else {
                            KuiTheme.colorScheme.onSurfaceVariant
                        },
                    )
                },
            )
        }
    }
}

@Composable
fun KuiAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    tone: KuiTone = KuiTone.Muted,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(kuiContainerColor(tone), CircleShape)
            .border(1.dp, kuiBorderColor(tone), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials.take(2).uppercase(),
            style = KuiTheme.typography.labelMedium,
            color = kuiContentColor(tone),
        )
    }
}

@Composable
fun KuiLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = KuiTheme.typography.labelMedium,
        color = KuiTheme.colorScheme.onSurface,
    )
}

@Composable
fun KuiSeparator(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier = modifier, color = KuiTheme.colorScheme.outline)
}

@Composable
private fun kuiContainerColor(tone: KuiTone): Color = when (tone) {
    KuiTone.Default -> KuiTheme.colorScheme.surface
    KuiTone.Accent -> KuiTheme.colorScheme.primary
    KuiTone.Muted -> KuiTheme.colors.surfaceSoft
    KuiTone.Success -> KuiTheme.colors.successContainer
    KuiTone.Warning -> KuiTheme.colors.warningContainer
    KuiTone.Info -> KuiTheme.colors.infoContainer
    KuiTone.Danger -> KuiTheme.colorScheme.errorContainer
}

@Composable
private fun kuiContentColor(tone: KuiTone): Color = when (tone) {
    KuiTone.Default -> KuiTheme.colorScheme.onSurface
    KuiTone.Accent -> KuiTheme.colorScheme.onPrimary
    KuiTone.Muted -> KuiTheme.colorScheme.onSurfaceVariant
    KuiTone.Success -> KuiTheme.colors.onSuccessContainer
    KuiTone.Warning -> KuiTheme.colors.onWarningContainer
    KuiTone.Info -> KuiTheme.colors.onInfoContainer
    KuiTone.Danger -> KuiTheme.colorScheme.onErrorContainer
}

@Composable
private fun kuiBorderColor(tone: KuiTone): Color = when (tone) {
    KuiTone.Default -> KuiTheme.colorScheme.outline
    KuiTone.Accent -> KuiTheme.colorScheme.primary
    KuiTone.Muted -> KuiTheme.colors.surfaceStrong
    KuiTone.Success -> KuiTheme.colors.success
    KuiTone.Warning -> KuiTheme.colors.warning
    KuiTone.Info -> KuiTheme.colors.info
    KuiTone.Danger -> KuiTheme.colorScheme.error
}

private fun KuiTone.toBadgeTone(): KuiBadgeTone = when (this) {
    KuiTone.Default -> KuiBadgeTone.Default
    KuiTone.Accent -> KuiBadgeTone.Accent
    KuiTone.Muted -> KuiBadgeTone.Muted
    KuiTone.Success -> KuiBadgeTone.Success
    KuiTone.Warning -> KuiBadgeTone.Warning
    KuiTone.Info -> KuiBadgeTone.Info
    KuiTone.Danger -> KuiBadgeTone.Danger
}
