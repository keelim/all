package com.keelim.arducon.ui.screen

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.keelim.arducon.ui.component.AdBannerView

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
    var url by remember { mutableStateOf("https://") }
    var previewData by remember { mutableStateOf<OgTagData?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = { newValue ->
                url = if (newValue.isEmpty()) {
                    "https://"
                } else {
                    newValue
                }
                errorMessage = null
            },
            label = { Text("URL 입력") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null,
            supportingText = {
                errorMessage?.let { Text(it) }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        url = "https://"
                        errorMessage = null
                        previewData = null
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear URL"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    val uri = Uri.parse(url)
                    if (uri.scheme == null || uri.host == null) {
                        errorMessage = "올바른 URL 형식이 아닙니다"
                        return@Button
                    }
                    parseTag(url) { data ->
                        previewData = data
                        errorMessage = null
                    }
                } catch (e: Exception) {
                    errorMessage = "올바른 URL 형식이 아닙니다"
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

        Spacer(modifier = Modifier.weight(1f))

        AdBannerView(
            modifier = Modifier.fillMaxWidth(),
        )
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



