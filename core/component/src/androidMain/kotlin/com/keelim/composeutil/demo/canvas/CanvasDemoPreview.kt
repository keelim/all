package com.keelim.composeutil.demo.canvas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun PreviewDrawCanvas() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        val modifier = Modifier.size(100.dp)
        item {
            DrawLine(
                modifier = modifier,
            )
        }
        item {
            DrawRect(
                modifier = modifier,
            )
        }
        item {
            DrawText(
                text = "Hello, World!",
                modifier = modifier,
            )
        }
        item {
            Rotate(
                modifier = modifier,
            )
        }
        item {
            DrawCircle(
                modifier = modifier,
            )
        }
        item {
            DrawOval(
                modifier = modifier,
            )
        }
        item {
            DrawTriangle(
                modifier = modifier,
            )
        }
        item {
            DrawArc(
                color = Color.Blue,
                modifier = modifier,
            )
        }
    }
}
