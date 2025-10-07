package com.keelim.composeutil.component.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.util.randomColor


@Preview
@Composable
fun PreviewFlipCard() {
    FlipCard(
        title = "ullamcorper",
        name = "Meghan Roberson",
        description = "arcu",
        title2 = "elementum",
        subtitle = "natum",
    )
}

@Preview
@Composable
fun PreviewFontCard() {
    FrontCard(
        title = "ullamcorper",
        name = "Meghan Roberson",
        description = "arcu",
        color = randomColor(),
    )
}

@Preview
@Composable
fun PreviewBackCard() {
    BackCard(
        title = "elementum",
        subtitle = "natum",
    )
}
