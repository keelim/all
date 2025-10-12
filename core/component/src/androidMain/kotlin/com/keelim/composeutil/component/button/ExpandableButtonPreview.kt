package com.keelim.composeutil.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewExpandableButton() {
    ExpandableButton(
        title = "pellentesque",
        subtitle = "parturient",
        buttonHint = "postea",
        clickedButtonHint = "periculis",
        onClick = {},
    )
}
