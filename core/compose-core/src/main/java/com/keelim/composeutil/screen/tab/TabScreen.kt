package com.keelim.composeutil.screen.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEachIndexed

@Composable
fun TabScreen() {
    var tabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Home", "About", "Settings", "More", "Something", "Everything")
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
        ) {
            tabs.fastForEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                            )

                            1 -> Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                            )

                            2 -> Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                            )

                            3 -> Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                            )

                            4 -> Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                            )

                            5 -> Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                            )
                        }
                    },
                )
            }
        }
        // when (tabIndex) {
        //     0 -> HomeScreen()
        //     1 -> AboutScreen()
        //     2 -> SettingsScreen()
        // }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TabScreenPreview() {
    TabScreen()
}
