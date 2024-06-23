package com.keelim.arducon.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space8
import com.keelim.core.database.model.DeepLink

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel()
) {
    val items by viewModel.deepLinkList.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )
    MainScreen(
        items = items,
        onSearch = viewModel::onClickSearch,
        onDelete = viewModel::deleteDeepLinkUrl,
    )
}

@Composable
fun MainScreen(
    items: List<DeepLink>,
    onSearch: (String) -> Unit,
    onDelete: (DeepLink) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        val (text, setText) = remember { mutableStateOf("") }

        Row(
            modifier = Modifier.padding(top = space8)
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = setText,
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable { setText("") }
                        )
                    }
                },
                )
            Spacer(
                modifier = Modifier.width(space8)
            )
            Button(
                onClick = { onSearch(text) },
            ) {
                Text("Search")
            }
        }
        HorizontalDivider()
        DeepLinkSection(
            items = items,
            onDelete = onDelete,
        )
    }
}


@Preview
@Composable
private fun PreviewMainScreen() {
    MainScreen(
        onSearch = {},
        onDelete = {},
        items = listOf(
            DeepLink("https://www.google.com", 0),
            DeepLink("https://www.naver.com", 0),
            DeepLink("https://www.daum.net", 0),
        )
    )
}
