package com.keelim.composeutil.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space4

@Preview
@Composable
private fun SwitchButtonPreview() {
    SwitchButton(
        width = 100.dp,
        height = 30.dp,
        padding = space4,
        isEnable = false,
    )
}
