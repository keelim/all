@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.composeutil.demo.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StickyHeaderDemo() {
    val items = remember { listOf("A", "B", "C") }
    LazyColumn {
        items.forEach { item ->
            stickyHeader {
                Column(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray),
                ) {
                    KuiText(
                        text = item,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
