package com.keelim.composeutil.demo.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space2
import kotlinx.coroutines.launch

@Composable
fun VerticalListDemo() {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row {
            KuiButton(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(space2),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
            ) {
                KuiText("Top")
            }
            KuiButton(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(space2),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(
                            scrollState.maxValue,
                        )
                    }
                },
            ) {
                KuiText("End")
            }
        }
        Column(Modifier.verticalScroll(scrollState)) {
            repeat(500) {
                KuiText(
                    text = "List ite $it",
                    style = KuiTheme.typography.titleLarge,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}
