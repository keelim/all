package com.keelim.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KuiTheme

data class KuiSelectOption(
    val value: String,
    val label: String,
    val enabled: Boolean = true,
)

data class KuiCalendarDay(
    val label: String,
    val selected: Boolean = false,
    val enabled: Boolean = true,
    val today: Boolean = false,
    val onClick: () -> Unit = {},
)

@Composable
fun KuiTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    minLines: Int = 3,
) {
    val colorScheme = KuiTheme.colorScheme
    val kuiColors = KuiTheme.colors
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        isError = isError,
        minLines = minLines,
        label = label?.let {
            {
                Text(
                    text = it,
                    style = KuiTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = KuiTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        },
        shape = KuiTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = kuiColors.surfaceSoft,
            focusedContainerColor = kuiColors.surfaceSoft,
            disabledContainerColor = kuiColors.surfaceSoft,
            unfocusedBorderColor = colorScheme.outline,
            focusedBorderColor = colorScheme.primary,
        ),
    )
}

@Composable
fun KuiCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun KuiSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun KuiRadioGroup(
    selectedValue: String,
    onSelected: (String) -> Unit,
    options: List<KuiSelectOption>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1)) {
        options.forEach { option ->
            KuiRadioGroupItem(
                selected = option.value == selectedValue,
                onClick = { onSelected(option.value) },
                label = option.label,
                enabled = option.enabled,
            )
        }
    }
}

@Composable
fun KuiRadioGroupItem(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
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
fun KuiSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
) {
    Slider(
        value = value.coerceIn(valueRange.start, valueRange.endInclusive),
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
    )
}

@Composable
fun KuiSelect(
    value: String?,
    options: List<KuiSelectOption>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Select",
    enabled: Boolean = true,
    label: String? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = options.firstOrNull { it.value == value }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1)) {
        if (label != null) {
            KuiLabel(text = label)
        }
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = KuiTheme.spacing.componentMd)
                .semantics { contentDescription = label ?: placeholder },
            enabled = enabled,
            shape = KuiTheme.shapes.medium,
            border = BorderStroke(1.dp, KuiTheme.colorScheme.outline),
        ) {
            Text(
                text = selected?.label ?: placeholder,
                modifier = Modifier.weight(1f),
                style = KuiTheme.typography.bodyMedium,
                color = if (selected == null) {
                    KuiTheme.colorScheme.onSurfaceVariant
                } else {
                    KuiTheme.colorScheme.onSurface
                },
            )
            Text(
                text = if (expanded) "^" else "v",
                style = KuiTheme.typography.labelSmall,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label,
                            style = KuiTheme.typography.bodyMedium,
                            color = KuiTheme.colorScheme.onSurface,
                        )
                    },
                    enabled = option.enabled,
                    onClick = {
                        onValueChange(option.value)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun KuiCalendar(
    days: List<KuiCalendarDay>,
    modifier: Modifier = Modifier,
    weekSize: Int = 7,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1),
    ) {
        days.chunked(weekSize.coerceAtLeast(1)).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1),
            ) {
                week.forEach { day ->
                    KuiCalendarDayButton(
                        day = day,
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat((weekSize - week.size).coerceAtLeast(0)) {
                    Surface(modifier = Modifier.weight(1f), color = KuiTheme.colorScheme.surface) {}
                }
            }
        }
    }
}

@Composable
fun KuiCalendarDayButton(
    day: KuiCalendarDay,
    modifier: Modifier = Modifier,
) {
    val container = when {
        day.selected -> KuiTheme.colorScheme.primary
        day.today -> KuiTheme.colors.surfaceStrong
        else -> KuiTheme.colorScheme.surface
    }
    val content = if (day.selected) KuiTheme.colorScheme.onPrimary else KuiTheme.colorScheme.onSurface

    Surface(
        onClick = day.onClick,
        modifier = modifier.sizeIn(minHeight = 40.dp),
        enabled = day.enabled,
        shape = KuiTheme.shapes.small,
        color = container,
        border = BorderStroke(1.dp, KuiTheme.colorScheme.outline),
    ) {
        Text(
            text = day.label,
            modifier = Modifier.padding(KuiTheme.spacing.space2),
            style = KuiTheme.typography.labelMedium,
            color = content,
            fontWeight = if (day.today || day.selected) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}
