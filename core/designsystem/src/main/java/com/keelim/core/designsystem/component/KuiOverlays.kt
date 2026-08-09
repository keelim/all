package com.keelim.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.keelim.core.designsystem.theme.KuiTheme

@Composable
fun KuiDialog(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    contentDescription: String? = title ?: description,
    confirmButton: (@Composable RowScope.() -> Unit)? = null,
    dismissButton: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    if (open) {
        Dialog(onDismissRequest = onDismissRequest) {
            KuiDialogContent(
                modifier = modifier.semantics {
                    this.contentDescription = contentDescription ?: "Dialog"
                },
            ) {
                if (title != null || description != null) {
                    KuiDialogHeader {
                        title?.let { KuiDialogTitle(text = it) }
                        description?.let { KuiDialogDescription(text = it) }
                    }
                }
                content()
                if (dismissButton != null || confirmButton != null) {
                    KuiDialogFooter {
                        dismissButton?.invoke(this)
                        confirmButton?.invoke(this)
                    }
                }
            }
        }
    }
}

@Composable
fun KuiDialogTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .semantics {
                contentDescription?.let { this.contentDescription = it }
            },
    ) {
        content()
    }
}

@Composable
fun KuiDialogContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp),
        shape = KuiTheme.shapes.large,
        color = KuiTheme.colorScheme.surface,
        tonalElevation = KuiTheme.elevation.panel,
        border = BorderStroke(1.dp, KuiTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(KuiTheme.spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space4),
            content = content,
        )
    }
}

@Composable
fun KuiDialogHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1),
        content = content,
    )
}

@Composable
fun KuiDialogFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = KuiTheme.spacing.space2,
            alignment = Alignment.End,
        ),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun KuiDialogTitle(
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
fun KuiDialogDescription(
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
fun KuiDialogClose(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    KuiButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = KuiButtonVariant.Ghost,
        enabled = enabled,
    )
}

@Composable
fun KuiAlertDialog(
    open: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    contentDescription: String = title,
) {
    if (open) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier.semantics { this.contentDescription = contentDescription },
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            title = {
                KuiAlertDialogTitle(text = title)
            },
            text = {
                KuiAlertDialogDescription(text = description)
            },
            containerColor = KuiTheme.colorScheme.surface,
            titleContentColor = KuiTheme.colorScheme.onSurface,
            textContentColor = KuiTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun KuiAlertDialogContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiDialogContent(modifier = modifier, content = content)
}

@Composable
fun KuiAlertDialogHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiDialogHeader(modifier = modifier, content = content)
}

@Composable
fun KuiAlertDialogFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    KuiDialogFooter(modifier = modifier, content = content)
}

@Composable
fun KuiAlertDialogTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    KuiDialogTitle(text = text, modifier = modifier)
}

@Composable
fun KuiAlertDialogDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    KuiDialogDescription(text = text, modifier = modifier)
}

@Composable
fun KuiAlertDialogAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    KuiButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = KuiButtonVariant.Primary,
        enabled = enabled,
    )
}

@Composable
fun KuiAlertDialogCancel(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    KuiButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = KuiButtonVariant.Ghost,
        enabled = enabled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KuiSheet(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    contentDescription: String? = title ?: description,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (open) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier.semantics {
                this.contentDescription = contentDescription ?: "Sheet"
            },
            sheetState = sheetState,
            containerColor = KuiTheme.colorScheme.surface,
            contentColor = KuiTheme.colorScheme.onSurface,
        ) {
            KuiSheetContent {
                if (title != null || description != null) {
                    KuiSheetHeader {
                        title?.let { KuiSheetTitle(text = it) }
                        description?.let { KuiSheetDescription(text = it) }
                    }
                }
                content()
            }
        }
    }
}

@Composable
fun KuiSheetTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    content: @Composable () -> Unit,
) {
    KuiDialogTrigger(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentDescription = contentDescription,
        content = content,
    )
}

@Composable
fun KuiSheetContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = KuiTheme.spacing.cardPadding,
                end = KuiTheme.spacing.cardPadding,
                bottom = KuiTheme.spacing.cardPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space4),
        content = content,
    )
}

@Composable
fun KuiSheetHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiDialogHeader(modifier = modifier, content = content)
}

@Composable
fun KuiSheetFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    KuiDialogFooter(modifier = modifier, content = content)
}

@Composable
fun KuiSheetTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    KuiDialogTitle(text = text, modifier = modifier)
}

@Composable
fun KuiSheetDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    KuiDialogDescription(text = text, modifier = modifier)
}

@Composable
fun KuiSheetClose(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    KuiDialogClose(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun KuiTooltip(
    visible: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String = text,
    anchor: @Composable () -> Unit,
) {
    KuiAnchoredPopup(
        visible = visible,
        modifier = modifier,
        contentDescription = contentDescription,
        anchor = anchor,
    ) {
        KuiTooltipContent(text = text)
    }
}

@Composable
fun KuiTooltipContent(
    text: String,
    modifier: Modifier = Modifier,
) {
    KuiPopupSurface(modifier = modifier) {
        Text(
            text = text,
            style = KuiTheme.typography.bodySmall,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun KuiPopover(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    anchor: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiAnchoredPopup(
        visible = expanded,
        modifier = modifier,
        focusable = true,
        onDismissRequest = onDismissRequest,
        contentDescription = contentDescription,
        anchor = anchor,
    ) {
        KuiPopoverContent(content = content)
    }
}

@Composable
fun KuiPopoverContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiPopupSurface(modifier = modifier, content = content)
}

@Composable
fun KuiHoverCard(
    visible: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    anchor: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiAnchoredPopup(
        visible = visible,
        modifier = modifier,
        contentDescription = contentDescription,
        anchor = anchor,
    ) {
        KuiHoverCardContent(content = content)
    }
}

@Composable
fun KuiHoverCardContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiPopupSurface(modifier = modifier, content = content)
}

@Composable
fun KuiDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset.Zero,
    trigger: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier = modifier) {
        trigger()
        KuiDropdownMenuContent(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            offset = offset,
            content = content,
        )
    }
}

@Composable
fun KuiDropdownMenuTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    content: @Composable () -> Unit,
) {
    KuiDialogTrigger(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentDescription = contentDescription,
        content = content,
    )
}

@Composable
fun KuiDropdownMenuContent(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset.Zero,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.semantics { contentDescription = "Dropdown menu" },
        offset = offset,
        content = content,
    )
}

@Composable
fun KuiDropdownMenuGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier, content = content)
}

@Composable
fun KuiDropdownMenuRadioGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier, content = content)
}

@Composable
fun KuiDropdownMenuRadioGroup(
    selectedValue: String,
    onValueChange: (String) -> Unit,
    options: List<KuiSelectOption>,
    modifier: Modifier = Modifier,
) {
    KuiDropdownMenuRadioGroup(modifier = modifier) {
        options.forEach { option ->
            KuiDropdownMenuRadioItem(
                text = option.label,
                selected = option.value == selectedValue,
                onClick = { onValueChange(option.value) },
                enabled = option.enabled,
            )
        }
    }
}

@Composable
fun KuiDropdownMenuSub(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset.Zero,
    trigger: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = offset,
        trigger = trigger,
        content = content,
    )
}

@Composable
fun KuiDropdownMenuSubTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    content: @Composable () -> Unit,
) {
    KuiDropdownMenuTrigger(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentDescription = contentDescription,
        content = content,
    )
}

@Composable
fun KuiDropdownMenuSubContent(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset.Zero,
    content: @Composable ColumnScope.() -> Unit,
) {
    KuiDropdownMenuContent(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = offset,
        content = content,
    )
}

@Composable
fun KuiDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.onSurface,
            )
        },
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingContent,
        trailingIcon = trailingContent,
    )
}

@Composable
fun KuiDropdownMenuCheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    KuiDropdownMenuItem(
        text = text,
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        enabled = enabled,
        leadingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = null,
                enabled = enabled,
            )
        },
    )
}

@Composable
fun KuiDropdownMenuRadioItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    KuiDropdownMenuItem(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        leadingContent = {
            RadioButton(
                selected = selected,
                onClick = null,
                enabled = enabled,
            )
        },
    )
}

@Composable
fun KuiDropdownMenuLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.padding(
            horizontal = KuiTheme.spacing.space4,
            vertical = KuiTheme.spacing.space2,
        ),
        style = KuiTheme.typography.labelMedium,
        color = KuiTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
fun KuiDropdownMenuSeparator(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(vertical = KuiTheme.spacing.space1),
        color = KuiTheme.colorScheme.outline,
    )
}

@Composable
fun KuiDropdownMenuShortcut(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = KuiTheme.typography.labelSmall,
        color = KuiTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun KuiAnchoredPopup(
    visible: Boolean,
    modifier: Modifier,
    focusable: Boolean = false,
    onDismissRequest: (() -> Unit)? = null,
    contentDescription: String? = null,
    anchor: @Composable () -> Unit,
    popupContent: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        anchor()
        if (visible) {
            Popup(
                alignment = Alignment.BottomStart,
                onDismissRequest = onDismissRequest,
                properties = PopupProperties(focusable = focusable),
            ) {
                Box(
                    modifier = Modifier.semantics {
                        contentDescription?.let { this.contentDescription = it }
                    },
                ) {
                    popupContent()
                }
            }
        }
    }
}

@Composable
private fun KuiPopupSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.widthIn(min = 160.dp, max = 320.dp),
        shape = KuiTheme.shapes.medium,
        color = KuiTheme.colorScheme.surface,
        tonalElevation = KuiTheme.elevation.panel,
        border = BorderStroke(1.dp, KuiTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(KuiTheme.spacing.space4),
            verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
            content = content,
        )
    }
}

@Composable
internal fun KuiOverlaysSample() {
    KuiDialog(open = false, onDismissRequest = {}, title = "Title")
    KuiAlertDialog(
        open = false,
        onDismissRequest = {},
        title = "Title",
        description = "Description",
        confirmButton = {},
    )
    KuiSheet(open = false, onDismissRequest = {}, title = "Title") {}
    KuiTooltip(visible = false, text = "Tooltip") {
        Text(
            text = "Anchor",
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
        )
    }
}
