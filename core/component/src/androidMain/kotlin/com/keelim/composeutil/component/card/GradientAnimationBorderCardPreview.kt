package com.keelim.composeutil.component.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PreviewGradientAnimationBorderCard() {
    Column {
        GradientAnimationBorderCard(
            colors = listOf(Color.Red, Color.Blue),
            duration = 5000,
            onClick = {},
            modifier = Modifier.padding(16.dp),
        ) {
            KuiText(
                modifier = Modifier.padding(16.dp),
                text = "Hello, World!",
            )
        }
    }
}
