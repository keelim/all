package com.keelim.composeutil.demo

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SwipeToDismiss(
    modifier: Modifier = Modifier,
) {
    val state = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(
        modifier = modifier,
        state = state,
        backgroundContent = {
            val color by animateColorAsState(
                when (state.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.LightGray
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                }
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
            )
        },
    ) {
        Card {
            ListItem(
                headlineContent = {
                    Text("Cupcake")
                },
                supportingContent = { Text("Swipe me left or right!") }
            )
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
private fun PreviewSwipeToDismiss() {
    SwipeToDismiss()
}
