package com.keelim.composeutil.component.etc.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewPriceCard() {
    PriceCard(
        priceCardState = PriceCardState(
            value = 2.3f,
            suffix = "adolescens",
            previews = listOf(
                Pair("Jan", 15000f),
                Pair("Feb", 20000f),
                Pair("Mar", 38000f),
                Pair("Apr", 8000f),
                Pair("May", 10000f),
            ),
        ),
    )
}
