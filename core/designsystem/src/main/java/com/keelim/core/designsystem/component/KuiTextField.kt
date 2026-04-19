package com.keelim.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keelim.core.designsystem.theme.KuiTheme

@Composable
fun KuiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
) {
    val kuiColors = KuiTheme.colors
    val colorScheme = KuiTheme.colorScheme
    val spacing = KuiTheme.spacing
    val labelStyle = KuiTheme.typography.labelMedium
    val placeholderStyle = KuiTheme.typography.bodyMedium

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = spacing.componentLg),
        enabled = enabled,
        isError = isError,
        singleLine = singleLine,
        label = label?.let {
            {
                Text(
                    text = it,
                    style = labelStyle,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = placeholderStyle,
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
            unfocusedLabelColor = colorScheme.onSurfaceVariant,
            focusedLabelColor = colorScheme.primary,
            unfocusedPlaceholderColor = colorScheme.onSurfaceVariant,
        ),
    )
}
