@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.arducon.ui.screen.main

import android.content.Intent
import android.webkit.URLUtil
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.keelim.composeutil.component.icon.rememberQrCodeScanner
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.model.DeepLink
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun MainRoute(
    onShowMessage: (String) -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateSaastatus: () -> Unit,
    onNavigateOgTagPreview: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val schemeList by viewModel.schemeList.collectAsStateWithLifecycle()
    val items by viewModel.deepLinkList.collectAsStateWithLifecycle()
    val isSearched = viewModel.onClickSearch.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showBottomSheet.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(isSearched.value) {
        if (isSearched.value.isEmpty()) return@LaunchedEffect
        try {
            Intent(
                Intent.ACTION_VIEW,
                isSearched.value.toUri(),
            ).let { context.startActivity(it) }
            viewModel.clear()
        } catch (throwable: Throwable) {
            onShowMessage(
                "Exception !!!\n" + throwable.message.toString(),
            )
        }
    }

    MainScreen(
        schemeList = schemeList,
        favoriteItems = items.first,
        generalItems = items.second,
        onSearch = viewModel::onClickSearch,
        onUpdate = viewModel::updateDeepLinkUrl,
        onDelete = viewModel::deleteDeepLinkUrl,
        onItemLongClick = viewModel::onItemLongClick,
        onQrCodeClick = onQrCodeClick,
        onNavigateSearch = onNavigateSearch,
        onRegister = viewModel::onRegister,
        onNavigateSaastatus = onNavigateSaastatus,
        onNavigateOgTagPreview = onNavigateOgTagPreview,
        onDeleteScheme = viewModel::deleteScheme,
    )

    if (showBottomSheet != DeepLink.EMPTY) {
        DeepLinkBottomSheet(
            deepLink = showBottomSheet,
            onDismiss = viewModel::hideBottomSheet,
            onDelete = viewModel::deleteDeepLinkUrl,
        )
    }
}

@Composable
fun MainScreen(
    favoriteItems: List<DeepLink>,
    generalItems: List<DeepLink>,
    schemeList: List<String>,
    onSearch: (String, String) -> Unit,
    onUpdate: (DeepLink) -> Unit,
    onDelete: (DeepLink) -> Unit,
    onItemLongClick: (DeepLink) -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSearch: () -> Unit,
    onRegister: (String) -> Unit,
    onNavigateSaastatus: () -> Unit,
    onNavigateOgTagPreview: () -> Unit,
    onDeleteScheme: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    val isScrollInProgress = remember {
        derivedStateOf {
            listState.isScrollInProgress || listState.canScrollForward.not()
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = space16),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = space16),
                verticalArrangement = Arrangement.spacedBy(space4),
            ) {
                Text(
                    text = "Arducon",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Deeplink Tester",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(visible = isScrollInProgress.value.not()) {
                HorizontalFloatingToolbarSection(
                    onNavigateOgTagPreview = onNavigateOgTagPreview,
                    onQrCodeClick = onQrCodeClick,
                    onNavigateSearch = onNavigateSearch,
                    onNavigateSaastatus = onNavigateSaastatus,
                )
            }
        },
    ) { paddingValues ->
        DeepLinkSection(
            schemeList = schemeList,
            favoriteItems = favoriteItems,
            generalItems = generalItems,
            onSearch = onSearch,
            onRegister = onRegister,
            onDeleteScheme = onDeleteScheme,
            onUpdate = onUpdate,
            onDelete = onDelete,
            onItemLongClick = onItemLongClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            listState = listState
        )
    }
}

@Composable
private fun HorizontalFloatingToolbarSection(
    onNavigateOgTagPreview: () -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateSaastatus: () -> Unit,
) {
    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }

    HorizontalFloatingToolbar(
        expanded = isExpanded
    ) {
        IconButton(
            onClick = onNavigateOgTagPreview,
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "OG Tag Preview",
            )
        }
        IconButton(
            onClick = onQrCodeClick,
        ) {
            Icon(
                imageVector = rememberQrCodeScanner(
                    tintColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                ),
                contentDescription = "QR Code Scanner",
            )
        }
        IconButton(
            onClick = onNavigateSearch,
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
            )
        }
        IconButton(
            onClick = onNavigateSaastatus,
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "navigate saastatus",
            )
        }
    }
}

@Composable
private fun DeepLinkBottomSheet(
    deepLink: DeepLink,
    onDismiss: () -> Unit,
    onDelete: (DeepLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = space16, vertical = space8),
            verticalArrangement = Arrangement.spacedBy(space12),
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(space16)
                ) {
                    deepLink.imageUrl.takeIf { it.isNotEmpty() }?.let { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Spacer(modifier = Modifier.height(space16))
                    }

                    Text(
                        text = deepLink.title.takeIf { it.isNotEmpty() } ?: "제목 없음",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(space8))

                    val context = LocalContext.current
                    Text(
                        text = deepLink.url,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            if (URLUtil.isValidUrl(deepLink.url)) {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        deepLink.url.toUri()
                                    )
                                )
                            } else {
                                Toast.makeText(context, "유효하지 않은 URL입니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (deepLink.isBookMarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "즐겨찾기",
                        tint = if (deepLink.isBookMarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(space24)
                    )
                    Spacer(modifier = Modifier.width(space8))
                    Text(
                        text = if (deepLink.isBookMarked) "즐겨찾기 추가됨" else "즐겨찾기 아님",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                val formattedTimestamp = remember(deepLink.timestamp) {
                    val instant = Instant.fromEpochMilliseconds(deepLink.timestamp)
                    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                    "${dateTime.year}년 ${dateTime.monthNumber}월 ${dateTime.dayOfMonth}일 ${
                        String.format(
                            "%02d",
                            dateTime.hour
                        )
                    }:${String.format("%02d", dateTime.minute)}"
                }
                Text(
                    text = "생성일: $formattedTimestamp",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // OutlinedButton(
                //     onClick = { /* TODO: 편집 기능 구현 */ },
                //     modifier = Modifier.weight(1f),
                //     shape = RoundedCornerShape(8.dp)
                // ) {
                //     Text("편집")
                // }
                // Spacer(modifier = Modifier.width(space16))
                TextButton(
                    onClick = {
                        onDelete(deepLink)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("삭제")
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    MainScreen(
        schemeList = listOf("http", "https"),
        onSearch = { _, _ -> },
        onUpdate = {},
        onDelete = {},
        favoriteItems = listOf(
            DeepLink("https://www.google.com", 0),
            DeepLink("https://www.naver.com", 0),
            DeepLink("https://www.daum.net", 0),
        ),
        generalItems = listOf(
            DeepLink("https://www.google.com", 0),
            DeepLink("https://www.naver.com", 0),
            DeepLink("https://www.daum.net", 0),
        ),
        onQrCodeClick = {},
        onNavigateSearch = {},
        onRegister = {},
        onNavigateSaastatus = {},
        onNavigateOgTagPreview = {},
        onDeleteScheme = {},
        onItemLongClick = { },
    )
}
