package com.keelim.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KuiTheme

enum class KuiToastTone { Default, Success, Warning, Info, Danger }

data class KuiAccordionItemState(
    val value: String,
    val title: String,
    val description: String? = null,
    val enabled: Boolean = true,
)

@Composable
fun KuiToastHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    tone: KuiToastTone = KuiToastTone.Default,
    dismissLabel: String? = null,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { data ->
        KuiToast(
            snackbarData = data,
            tone = tone,
            dismissLabel = dismissLabel,
        )
    }
}

@Composable
fun KuiToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    tone: KuiToastTone = KuiToastTone.Default,
    dismissLabel: String? = null,
) {
    KuiToast(
        message = snackbarData.visuals.message,
        modifier = modifier,
        tone = tone,
        actionLabel = snackbarData.visuals.actionLabel,
        onAction = snackbarData::performAction,
        dismissLabel = dismissLabel,
        onDismiss = snackbarData::dismiss,
    )
}

@Composable
fun KuiToast(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    tone: KuiToastTone = KuiToastTone.Default,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    dismissLabel: String? = null,
    onDismiss: (() -> Unit)? = null,
) {
    Snackbar(
        modifier = modifier.semantics { contentDescription = title ?: message },
        action = actionLabel?.let { label ->
            {
                KuiToastAction(
                    label = label,
                    onClick = { onAction?.invoke() },
                )
            }
        },
        dismissAction = dismissLabel?.let { label ->
            {
                KuiToastClose(
                    label = label,
                    onClick = { onDismiss?.invoke() },
                )
            }
        },
        containerColor = kuiToastContainerColor(tone),
        contentColor = kuiToastContentColor(tone),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1)) {
            title?.let { KuiToastTitle(text = it, tone = tone) }
            KuiToastDescription(text = message, tone = tone)
        }
    }
}

@Composable
fun KuiToastTitle(
    text: String,
    modifier: Modifier = Modifier,
    tone: KuiToastTone = KuiToastTone.Default,
) {
    Text(
        text = text,
        modifier = modifier,
        style = KuiTheme.typography.labelLarge,
        color = kuiToastContentColor(tone),
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
fun KuiToastDescription(
    text: String,
    modifier: Modifier = Modifier,
    tone: KuiToastTone = KuiToastTone.Default,
) {
    Text(
        text = text,
        modifier = modifier,
        style = KuiTheme.typography.bodyMedium,
        color = kuiToastContentColor(tone),
    )
}

@Composable
fun KuiToastAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(
            text = label,
            style = KuiTheme.typography.labelLarge,
            color = KuiTheme.colorScheme.primary,
        )
    }
}

@Composable
fun KuiToastClose(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(
            text = label,
            style = KuiTheme.typography.labelLarge,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun KuiAccordion(
    items: List<KuiAccordionItemState>,
    expandedValues: Set<String>,
    onExpandedValuesChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    multiple: Boolean = false,
    itemContent: @Composable ColumnScope.(KuiAccordionItemState) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
    ) {
        items.forEach { item ->
            val expanded = item.value in expandedValues
            KuiAccordionItem(
                expanded = expanded,
                onExpandedChange = { nextExpanded ->
                    val nextValues = when {
                        multiple && nextExpanded -> expandedValues + item.value
                        multiple -> expandedValues - item.value
                        nextExpanded -> setOf(item.value)
                        else -> emptySet()
                    }
                    onExpandedValuesChange(nextValues)
                },
                enabled = item.enabled,
            ) {
                KuiAccordionTrigger(
                    text = item.title,
                    expanded = expanded,
                    onClick = {
                        val nextValues = when {
                            expanded -> expandedValues - item.value
                            multiple -> expandedValues + item.value
                            else -> setOf(item.value)
                        }
                        onExpandedValuesChange(nextValues)
                    },
                    description = item.description,
                    enabled = item.enabled,
                )
                KuiAccordionContent(expanded = expanded) {
                    itemContent(item)
                }
            }
        }
    }
}

@Composable
fun KuiAccordionItem(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = if (expanded) "Expanded" else "Collapsed"
            },
        shape = KuiTheme.shapes.medium,
        color = KuiTheme.colorScheme.surface,
        tonalElevation = KuiTheme.elevation.none,
        border = BorderStroke(1.dp, KuiTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = { onExpandedChange(!expanded) },
            ),
            content = content,
        )
    }
}

@Composable
fun KuiAccordionTrigger(
    text: String,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(KuiTheme.spacing.space4),
        horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1),
        ) {
            Text(
                text = text,
                style = KuiTheme.typography.titleSmall,
                color = KuiTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
            description?.let {
                Text(
                    text = it,
                    style = KuiTheme.typography.bodySmall,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            text = if (expanded) "^" else "v",
            style = KuiTheme.typography.labelLarge,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun KuiAccordionContent(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    AnimatedVisibility(visible = expanded, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = KuiTheme.spacing.space4,
                    end = KuiTheme.spacing.space4,
                    bottom = KuiTheme.spacing.space4,
                ),
            verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
            content = content,
        )
    }
}

@Composable
private fun kuiToastContainerColor(tone: KuiToastTone): Color = when (tone) {
    KuiToastTone.Default -> KuiTheme.colorScheme.inverseSurface
    KuiToastTone.Success -> KuiTheme.colors.successContainer
    KuiToastTone.Warning -> KuiTheme.colors.warningContainer
    KuiToastTone.Info -> KuiTheme.colors.infoContainer
    KuiToastTone.Danger -> KuiTheme.colorScheme.errorContainer
}

@Composable
private fun kuiToastContentColor(tone: KuiToastTone): Color = when (tone) {
    KuiToastTone.Default -> KuiTheme.colorScheme.inverseOnSurface
    KuiToastTone.Success -> KuiTheme.colors.onSuccessContainer
    KuiToastTone.Warning -> KuiTheme.colors.onWarningContainer
    KuiToastTone.Info -> KuiTheme.colors.onInfoContainer
    KuiToastTone.Danger -> KuiTheme.colorScheme.onErrorContainer
}

@Composable
internal fun KuiFeedbackSample() {
    KuiToast(message = "Message")
    KuiAccordion(
        items = listOf(KuiAccordionItemState(value = "one", title = "Title")),
        expandedValues = emptySet(),
        onExpandedValuesChange = {},
    ) {
        Text(
            text = it.title,
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}
