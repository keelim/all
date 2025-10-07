package com.keelim.composeutil.component.etc.divider

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PreviewDiagnoalDivider() {
    DiagnoalDivider(
        width = 100.dp,
        ratio = 0.2f,
        modifier = Modifier.height(32.dp),
        thickness = 2.dp,
        color = Color.Red,
    )
}
