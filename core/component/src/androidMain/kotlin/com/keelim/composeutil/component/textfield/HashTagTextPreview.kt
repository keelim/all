package com.keelim.composeutil.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8

@Composable
fun SampleHashTagScreen() {
    val (hashTags, setHashTags) = remember { mutableStateOf(emptyList<String>()) }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        HashTagTextField(onAdd = { setHashTags(hashTags + it) })
        Spacer(
            modifier = Modifier.height(space8),
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(space4),
        ) {
            items(hashTags) { item ->
                KuiText(
                    text = "# $item",
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSampleHashTagScreen() {
    SampleHashTagScreen()
}
