package com.keelim.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keelim.core.designsystem.theme.KuiTheme

enum class KuiButtonVariant { Primary, Secondary, Ghost, Danger }
enum class KuiButtonSize { Sm, Md, Lg }

@Composable
fun KuiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: KuiButtonVariant = KuiButtonVariant.Primary,
    size: KuiButtonSize = KuiButtonSize.Md,
    enabled: Boolean = true,
) {
    val spacing = KuiTheme.spacing
    val minHeight = when (size) {
        KuiButtonSize.Sm -> spacing.componentSm
        KuiButtonSize.Md -> spacing.componentMd
        KuiButtonSize.Lg -> spacing.componentLg
    }
    val horizontalPadding = when (size) {
        KuiButtonSize.Sm -> spacing.space4
        KuiButtonSize.Md -> spacing.space6
        KuiButtonSize.Lg -> spacing.cardPadding
    }
    val contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = spacing.none)
    val kuiColors = KuiTheme.colors
    val colorScheme = KuiTheme.colorScheme
    val shape = KuiTheme.shapes.small
    val labelStyle = KuiTheme.typography.labelLarge

    when (variant) {
        KuiButtonVariant.Primary -> Button(
            onClick = onClick,
            modifier = modifier.heightIn(min = minHeight),
            enabled = enabled,
            shape = shape,
            contentPadding = contentPadding,
        ) {
            Text(
                text = text,
                style = labelStyle,
                color = colorScheme.onPrimary,
            )
        }

        KuiButtonVariant.Secondary -> OutlinedButton(
            onClick = onClick,
            modifier = modifier.heightIn(min = minHeight),
            enabled = enabled,
            shape = shape,
            contentPadding = contentPadding,
        ) {
            Text(
                text = text,
                style = labelStyle,
                color = colorScheme.primary,
            )
        }

        KuiButtonVariant.Ghost -> TextButton(
            onClick = onClick,
            modifier = modifier.heightIn(min = minHeight),
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.textButtonColors(
                containerColor = kuiColors.surfaceSoft,
                contentColor = colorScheme.onSurface,
            ),
            contentPadding = contentPadding,
        ) {
            Text(
                text = text,
                style = labelStyle,
                color = colorScheme.onSurface,
            )
        }

        KuiButtonVariant.Danger -> Button(
            onClick = onClick,
            modifier = modifier.heightIn(min = minHeight),
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.error,
                contentColor = colorScheme.onError,
            ),
            contentPadding = contentPadding,
        ) {
            Text(
                text = text,
                style = labelStyle,
                color = colorScheme.onError,
            )
        }
    }
}
