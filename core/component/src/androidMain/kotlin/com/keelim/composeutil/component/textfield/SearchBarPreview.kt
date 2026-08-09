package com.keelim.composeutil.component.textfield

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewSearchBar() {
    SearchBar(value = "electram", onValueChange = {}, placeHolder = "inani")
}
