package com.keelim.composeutil.component.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DotIndicator(size: Dp, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
    )
}

@Preview(showBackground = true)
@Composable
private fun DotIndicatorPreview() {
    DotIndicator(size = 4.dp, color = Color.Black)
}

@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    dotCount: Int,
    dotSize: Dp,
    selectedIndex: Int,
    selectedColor: Color = Color.Black,
    unselectedColor: Color = Color.White,
) {
    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
    ) {
        items(dotCount) { index ->
            DotIndicator(
                size = dotSize,
                color =
                if (index == selectedIndex) {
                    selectedColor
                } else {
                    unselectedColor
                },
            )

            if (index != dotCount - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DotsIndicatorPreview() {
    DotsIndicator(
        dotCount = 5,
        dotSize = 4.dp,
        selectedIndex = 3,
    )
}
