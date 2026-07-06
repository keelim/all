package com.keelim.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KuiTheme

@Composable
fun KuiCardHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1),
    ) {
        content()
    }
}

@Composable
fun KuiCardTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = KuiTheme.typography.titleLarge,
        color = KuiTheme.colorScheme.onSurface,
    )
}

@Composable
fun KuiCardDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = KuiTheme.typography.bodyMedium,
        color = KuiTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
fun KuiCardContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        content()
    }
}

@Composable
fun KuiCardFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
        content = content,
    )
}

@Composable
fun KuiMetricValue(
    value: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    tone: KuiTone = KuiTone.Default,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1)) {
        if (label != null) {
            Text(
                text = label,
                style = KuiTheme.type.overline,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
        }
        KuiDataNumber(value = value, tone = tone)
    }
}

@Composable
fun KuiAlertTitle(
    text: String,
    modifier: Modifier = Modifier,
    tone: KuiTone = KuiTone.Default,
) {
    Text(
        text = text,
        modifier = modifier,
        style = KuiTheme.typography.titleMedium,
        color = kuiToneTextColor(tone),
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
fun KuiAlertDescription(
    text: String,
    modifier: Modifier = Modifier,
    tone: KuiTone = KuiTone.Default,
) {
    Text(
        text = text,
        modifier = modifier,
        style = KuiTheme.typography.bodyMedium,
        color = kuiToneTextColor(tone),
    )
}

@Composable
fun KuiBreadcrumb(
    items: List<String>,
    modifier: Modifier = Modifier,
    separator: String = "/",
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
    ) {
        items.forEachIndexed { index, item ->
            Text(
                text = item,
                style = KuiTheme.typography.labelMedium,
                color = if (index == items.lastIndex) {
                    KuiTheme.colorScheme.onSurface
                } else {
                    KuiTheme.colorScheme.onSurfaceVariant
                },
            )
            if (index < items.lastIndex) {
                Text(
                    text = separator,
                    style = KuiTheme.typography.labelMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun KuiTable(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
    ) {
        content()
    }
}

@Composable
fun KuiTableHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        content()
    }
}

@Composable
fun KuiTableBody(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        content()
    }
}

@Composable
fun KuiTableFooter(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.background(KuiTheme.colors.surfaceSoft),
    ) {
        content()
    }
}

@Composable
fun KuiTableRow(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (selected) KuiTheme.colors.surfaceSoft else KuiTheme.colorScheme.surface),
        content = content,
    )
}

@Composable
fun RowScope.KuiTableHead(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .weight(1f)
            .widthIn(min = 96.dp),
        color = KuiTheme.colorScheme.surface,
        border = BorderStroke(0.5.dp, KuiTheme.colorScheme.outline),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(KuiTheme.spacing.space3),
            style = KuiTheme.typography.labelMedium,
            color = KuiTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun RowScope.KuiTableCell(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .weight(1f)
            .widthIn(min = 96.dp),
        color = KuiTheme.colorScheme.surface,
        border = BorderStroke(0.5.dp, KuiTheme.colorScheme.outline),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(KuiTheme.spacing.space3),
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun KuiTableCaption(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.padding(top = KuiTheme.spacing.space2),
        style = KuiTheme.typography.bodySmall,
        color = KuiTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun kuiToneTextColor(tone: KuiTone) = when (tone) {
    KuiTone.Default -> KuiTheme.colorScheme.onSurface
    KuiTone.Accent -> KuiTheme.colorScheme.primary
    KuiTone.Muted -> KuiTheme.colorScheme.onSurfaceVariant
    KuiTone.Success -> KuiTheme.colors.onSuccessContainer
    KuiTone.Warning -> KuiTheme.colors.onWarningContainer
    KuiTone.Info -> KuiTheme.colors.onInfoContainer
    KuiTone.Danger -> KuiTheme.colorScheme.onErrorContainer
}
