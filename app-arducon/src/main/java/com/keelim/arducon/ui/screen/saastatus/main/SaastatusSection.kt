package com.keelim.arducon.ui.screen.saastatus.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space4

@Composable
fun SaastatusEmpty(
    onRegister: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        KuiText(
            text = "지원 서비스 없음",
            style = KuiTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(
            modifier = Modifier.height(space4),
        )
        KuiText(
            text = "서비스를 등록해보세요.",
            style = KuiTheme.typography.bodyMedium,
        )

        KuiIconButton(
            onClick = onRegister,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            KuiIcon(imageVector = Icons.Filled.AddCircle, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun PreviewSaastatusEmpty() {
    SaastatusEmpty(
        onRegister = {},
    )
}
