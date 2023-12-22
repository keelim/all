package com.keelim.composeutil.component.tab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MultiTab(
    tabs: ImmutableList<String>,
) {
    val selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        indicator = { positions ->
        },
    ) {
        tabs.forEachIndexed { index, data ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = {},
            ) {
                Text(
                    text = data,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMultiTab() {
    MultiTab(
        persistentListOf(
            "home1",
            "home1",
            "home1",
            "home1",
            "home1",
        ),
    )
}
