package com.keelim.composeutil.demo.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space2
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VerticalListDemo() {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row {
            Button(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(space2),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
            ) {
                Text("Top")
            }
            Button(
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
                Text("End")
            }
        }
        Column(Modifier.verticalScroll(scrollState)) {
            repeat(500) {
                Text(
                    text = "List ite $it",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun VerticalListDemoPreview() {
    VerticalListDemo()
}
