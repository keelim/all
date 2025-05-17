package com.keelim.arducon.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

data class OgTagData(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = ""
)

@Composable
fun OgTagPreviewRoute(
    viewModel: OgTagPreviewViewModel = hiltViewModel(),
    onNavigateToBrowser: (String) -> Unit
) {
    OgTagPreviewScreen(
        parseTag = viewModel::parseOgTags,
        onNavigateToBrowser = onNavigateToBrowser
    )
}

@Composable
fun OgTagPreviewScreen(
    parseTag: (url: String, (OgTagData) -> Unit) -> Unit,
    onNavigateToBrowser: (String) -> Unit
) {
    var url by remember { mutableStateOf("") }
    var previewData by remember { mutableStateOf<OgTagData?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("URL 입력") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                parseTag(url) { data ->
                    previewData = data
                }
            }
        ) {
            Text("미리보기")
        }

        Spacer(modifier = Modifier.height(16.dp))

        previewData?.let { data ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToBrowser(url) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = data.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = data.description, style = MaterialTheme.typography.bodyMedium)
                    AsyncImage(
                        model = data.imageUrl,
                        contentDescription = "Preview Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OgTagPreviewScreenPreview() {
    MaterialTheme {
        OgTagPreviewScreen(
            parseTag = { _, callback ->
                callback(
                    OgTagData(
                        title = "미리보기 제목",
                        description = "미리보기 설명입니다. 이 텍스트는 미리보기용입니다.",
                        imageUrl = "https://picsum.photos/200/300"
                    )
                )
            },
            onNavigateToBrowser = {}
        )
    }
}



