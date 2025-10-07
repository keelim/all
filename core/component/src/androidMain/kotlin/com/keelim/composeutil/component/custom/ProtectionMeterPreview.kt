package com.keelim.composeutil.component.custom

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun PreviewProtectionMeter() {
    ProtectionMeter(
        inputValue = 32,
        subTitle = "Protection",
        progressColors = listOf(Color(0xFF00FF00), Color(0xFF00FFFF)),
        innerGradient = Color(0xFF00FF00),
    )
}
