package com.keelim.composeutil.component.tab

import androidx.compose.foundation.layout.fillMaxWidth
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiPrimaryTabRow
import com.keelim.core.designsystem.component.KuiTab
import com.keelim.core.designsystem.component.KuiText
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
    KuiPrimaryTabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
    ) {
        tabs.forEachIndexed { index, data ->
            KuiTab(
                selected = index == selectedTabIndex,
                onClick = {},
            ) {
                KuiText(
                    text = data,
                    style = KuiTheme.typography.titleMedium,
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
