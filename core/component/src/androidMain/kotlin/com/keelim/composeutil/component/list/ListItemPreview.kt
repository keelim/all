package com.keelim.composeutil.component.list


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun PreviewListItem(
    mockItems: List<AppItem> = (0..20).map { index ->
        AppItem(
            id = index,
            message = "index $index",
            state = if (index % 2 == 0) ItemState.Visible else ItemState.Progress,
        )
    },
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
    ) {
        items(
            items = mockItems,
            key = { it.id },
        ) { item ->
            ListItem(
                item = item,
                backgroundColor =
                if (item.id % 2 == 0) {
                    Color.LightGray
                } else {
                    Color.White
                },
                onItemClick = {
                },
            )
        }
    }
}
