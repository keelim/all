package com.keelim.composeutil.demo.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun TemperatureViewPreview() {
    TemperatureView(false, "", {}, {})
}

@Preview
@Composable
private fun InputRowPreview() {
    InputRow(false, "", {}, {})
}

