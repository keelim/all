package com.keelim.composeutil.component.button

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space24

@Preview
@Composable
fun PreviewSelectedButton() {
    SelectableButton(modifier = Modifier.size(space24), isSelected = true)
}

@Preview
@Composable
fun PreviewUnSelectedButton() {
    SelectableButton(modifier = Modifier.size(space24), isSelected = false)
}
