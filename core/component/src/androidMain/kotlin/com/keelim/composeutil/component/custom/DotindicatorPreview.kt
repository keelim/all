package com.keelim.composeutil.component.custom

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space4

@Preview
@Composable
private fun DotIndicatorPreview() {
    DotIndicator(size = space4, color = Color.Black)
}

@Preview
@Composable
private fun DotsIndicatorPreview() {
    DotsIndicator(
        dotCount = 5,
        dotSize = space4,
        selectedIndex = 3,
    )
}
