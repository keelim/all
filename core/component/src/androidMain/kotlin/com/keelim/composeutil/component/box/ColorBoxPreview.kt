package com.keelim.composeutil.component.box

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewColorBox() {
    ColorBox()
}

@Preview
@Composable
fun PreviewSimpleFlexBox() {
    SimpleFlexBox {
        for (i in 0..1000) {
            ColorBox()
        }
    }
}
