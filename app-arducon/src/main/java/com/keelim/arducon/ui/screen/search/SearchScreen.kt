package com.keelim.arducon.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.resource.space8

@Composable
fun SearchRoute(
    onUpdate: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    SearchScreen()

    LaunchedEffect(Unit) {
        onUpdate()
    }
}

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space8),
        ) {
            val (text, setText) = remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = setText,
                label = { Text("검색어를 입력해주세요") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        HorizontalDivider()
        // DeepLinkSearchSection(
        //     items = items,
        // )
    }
}

@Preview
@Composable
private fun PreviewSearchScreen() {
    SearchScreen()
}
