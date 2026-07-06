package com.keelim.arducon.ui.screen.ogtag

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiHorizontalDivider
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.core.designsystem.component.KuiOutlinedTextField
import com.keelim.core.designsystem.component.KuiSurface
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.keelim.composeutil.resource.space8

data class OgTagData(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
)

@Composable
fun OgTagPreviewRoute(
    viewModel: OgTagPreviewViewModel = hiltViewModel(),
    onNavigateToBrowser: (String) -> Unit,
) {
    OgTagPreviewScreen(
        parseTag = viewModel::parseOgTags,
        onNavigateToBrowser = onNavigateToBrowser,
    )
}

@Composable
fun OgTagPreviewScreen(
    parseTag: (url: String, (OgTagData) -> Unit) -> Unit,
    onNavigateToBrowser: (String) -> Unit,
) {
    var url by remember { mutableStateOf("https://") }
    var previewData by remember { mutableStateOf<OgTagData?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var urlInfo by remember { mutableStateOf<Uri?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 60.dp), // AdBannerView를 위한 여백
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KuiOutlinedTextField(
                value = url,
                onValueChange = { newValue ->
                    url = newValue.ifEmpty {
                        "https://"
                    }
                    errorMessage = null

                    urlInfo = try {
                        url.toUri()
                    } catch (e: Exception) {
                        null
                    }
                },
                label = { KuiText("URL 입력") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null,
                supportingText = {
                    errorMessage?.let { KuiText(it) }
                },
                trailingIcon = {
                    KuiIconButton(
                        onClick = {
                            url = "https://"
                            errorMessage = null
                            previewData = null
                            urlInfo = null
                        },
                    ) {
                        KuiIcon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear URL",
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(space8))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                KuiButton(
                    onClick = {
                        try {
                            val uri = url.toUri()
                            if (uri.scheme == null || uri.host == null) {
                                errorMessage = "올바른 URL 형식이 아닙니다"
                                return@KuiButton
                            }
                            urlInfo = uri
                            parseTag(url) { data ->
                                previewData = data
                                errorMessage = null
                            }
                        } catch (e: Exception) {
                            errorMessage = "올바른 URL 형식이 아닙니다"
                        }
                    },
                ) {
                    KuiText("미리보기")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            urlInfo?.let { uri ->
                KuiSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(KuiTheme.shapes.medium),
                    color = KuiTheme.colorScheme.surfaceVariant,
                    tonalElevation = 2.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp),
                        ) {
                            KuiIcon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = KuiTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            KuiText(
                                text = "URL 정보",
                                style = KuiTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = KuiTheme.colorScheme.primary,
                            )
                        }

                        KuiHorizontalDivider(
                            modifier = Modifier.padding(bottom = 12.dp),
                            color = KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                        )

                        UrlInfoItem("Scheme", uri.scheme)
                        UrlInfoItem("Host", uri.host)
                        UrlInfoItem("Path", uri.path)
                        UrlInfoItem("Query", uri.query)
                        UrlInfoItem("Fragment", uri.fragment)
                    }
                }
            }

            previewData?.let { data ->
                KuiSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToBrowser(url) }
                        .clip(RoundedCornerShape(12.dp)),
                    color = KuiTheme.colorScheme.surfaceVariant,
                    tonalElevation = 2.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp),
                        ) {
                            KuiIcon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = KuiTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            KuiText(
                                text = "OG 태그 미리보기",
                                style = KuiTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = KuiTheme.colorScheme.primary,
                            )
                        }

                        KuiHorizontalDivider(
                            modifier = Modifier.padding(bottom = 12.dp),
                            color = KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                        )

                        if (data.title.isNotEmpty()) {
                            KuiText(
                                text = data.title,
                                style = KuiTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp),
                            )
                        }

                        if (data.description.isNotEmpty()) {
                            KuiText(
                                text = data.description,
                                style = KuiTheme.typography.bodyMedium,
                                color = KuiTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 16.dp),
                            )
                        }

                        if (data.imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = data.imageUrl,
                                contentDescription = "Preview Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        KuiText(
                            text = "클릭하여 브라우저에서 열기",
                            style = KuiTheme.typography.bodySmall,
                            color = KuiTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.End),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UrlInfoItem(
    label: String,
    value: String?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        KuiText(
            text = "$label:",
            style = KuiTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = KuiTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.3f),
        )
        KuiText(
            text = value ?: "없음",
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.7f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OgTagPreviewScreenPreview() {
    KeelimDesignSystemTheme {
        OgTagPreviewScreen(
            parseTag = { _, callback ->
                callback(
                    OgTagData(
                        title = "미리보기 제목",
                        description = "미리보기 설명입니다. 이 텍스트는 미리보기용입니다.",
                        imageUrl = "https://picsum.photos/200/300",
                    ),
                )
            },
            onNavigateToBrowser = {},
        )
    }
}
