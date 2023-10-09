package com.keelim.composeutil.component.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppItem(
    val message: String,
    val id: Int,
    val state: ItemState,
)

sealed class ItemState {
    object Visible : ItemState()
    object Progress : ItemState()
    object Finish : ItemState()
}

@Composable
fun ListItem(
    item: AppItem,
    backgroundColor: Color,
    onItemClick: ((Int) -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundColor)
            .clickable {
                onItemClick?.invoke(item.id)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            text = item.message,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
        )
        Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
            when (val state = item.state) {
                ItemState.Visible, ItemState.Finish -> {
                    Icon(
                        imageVector = if (state is ItemState.Visible) {
                            Icons.Default.PlayArrow
                        } else {
                            Icons.Default.Check
                        },
                        contentDescription = "State Icon",
                        tint = if (state is ItemState.Visible) {
                            Color.Gray
                        } else {
                            Color.Blue
                        },
                    )
                }
                ItemState.Progress -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        strokeWidth = 3.dp,
                        color = Color.Magenta,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewListItem(
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
                    Log.d("[COMPOSE]", "Compose ListItem clicked")
                },
            )
        }
    }
}
