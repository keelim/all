package com.keelim.composeutil.component.appbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SearchViewPreview() {
    SearchView(query = "Test", onQueryChanged = { }, onSearch = { }, onClearQuery = { })
}
