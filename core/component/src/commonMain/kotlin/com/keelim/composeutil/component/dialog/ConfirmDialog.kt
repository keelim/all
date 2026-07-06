package com.keelim.composeutil.component.dialog

import com.keelim.composeutil.component.kui.KuiAlertDialog
import com.keelim.composeutil.component.kui.KuiButton
import com.keelim.composeutil.component.kui.KuiMaterialTheme
import com.keelim.composeutil.component.kui.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A reusable confirmation dialog component that follows Material 3 design guidelines.
 *
 * This dialog is used across multiple app modules to provide consistent user experience
 * for confirmation actions (delete, exit, etc.).
 *
 * @param title The dialog title text
 * @param message The dialog message/content text
 * @param onConfirm Callback when user confirms the action
 * @param onDismiss Callback when user dismisses the dialog
 * @param confirmText Text for the confirm button (default: "확인")
 * @param dismissText Text for the dismiss button (default: "취소")
 * @param modifier Optional modifier for the dialog
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "확인",
    dismissText: String = "취소",
) {
    KuiAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            KuiText(
                text = title,
                style = KuiMaterialTheme.typography.titleMedium,
                color = KuiMaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            KuiText(
                text = message,
                style = KuiMaterialTheme.typography.bodyMedium,
                color = KuiMaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            KuiButton(onClick = onConfirm) {
                KuiText(
                    text = confirmText,
                    style = KuiMaterialTheme.typography.labelLarge,
                    color = KuiMaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        dismissButton = {
            KuiButton(onClick = onDismiss) {
                KuiText(
                    text = dismissText,
                    style = KuiMaterialTheme.typography.labelLarge,
                    color = KuiMaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    )
}
