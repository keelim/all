package com.keelim.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KuiTheme

enum class KuiBadgeTone {
    Default, Muted, Accent, Danger, Success, Warning, Info
}

@Composable
fun KuiBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: KuiBadgeTone = KuiBadgeTone.Default,
) {
    val kuiColors = KuiTheme.colors
    val colorScheme = KuiTheme.colorScheme

    val bgColor = when (tone) {
        KuiBadgeTone.Default -> kuiColors.surfaceSoft
        KuiBadgeTone.Muted   -> kuiColors.surfaceSoft
        KuiBadgeTone.Accent  -> colorScheme.primary
        KuiBadgeTone.Danger  -> colorScheme.errorContainer
        KuiBadgeTone.Success -> kuiColors.successContainer
        KuiBadgeTone.Warning -> kuiColors.warningContainer
        KuiBadgeTone.Info    -> kuiColors.infoContainer
    }
    val fgColor = when (tone) {
        KuiBadgeTone.Default -> colorScheme.onSurface
        KuiBadgeTone.Muted   -> colorScheme.onSurfaceVariant
        KuiBadgeTone.Accent  -> colorScheme.onPrimary
        KuiBadgeTone.Danger  -> colorScheme.onErrorContainer
        KuiBadgeTone.Success -> kuiColors.onSuccessContainer
        KuiBadgeTone.Warning -> kuiColors.onWarningContainer
        KuiBadgeTone.Info    -> kuiColors.onInfoContainer
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = bgColor,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = KuiTheme.spacing.space2, vertical = 3.dp),
            style = KuiTheme.typography.labelSmall,
            color = fgColor,
            fontWeight = FontWeight.Medium,
        )
    }
}
