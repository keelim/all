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
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiPrimaryScrollableTabRow
import com.keelim.core.designsystem.component.KuiTab
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEachIndexed

@Composable
fun TabScreen() {
    var tabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Home", "About", "Settings", "More", "Something", "Everything")
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        KuiPrimaryScrollableTabRow(
            selectedTabIndex = tabIndex,
        ) {
            tabs.fastForEachIndexed { index, title ->
                KuiTab(
                    text = { KuiText(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> KuiIcon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                            )

                            1 -> KuiIcon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                            )

                            2 -> KuiIcon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                            )

                            3 -> KuiIcon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                            )

                            4 -> KuiIcon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                            )

                            5 -> KuiIcon(
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
