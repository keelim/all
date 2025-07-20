@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.arducon.ui.screen.main

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.webkit.URLUtil
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.keelim.arducon.ui.screen.main.MainViewModel.QrDialogState
import com.keelim.common.extensions.saveQrBitmapToGallery
import com.keelim.composeutil.component.icon.rememberQrCodeScanner
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
import com.keelim.model.DeepLink
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val appPermissions: List<String> by lazy {
    buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
fun MainRoute(
    onShowMessage: (String) -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateSaastatus: () -> Unit,
    onNavigateOgTagPreview: () -> Unit,
    onNavigateStats: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val schemeList by viewModel.schemeList.collectAsStateWithLifecycle()
    val items by viewModel.deepLinkList.collectAsStateWithLifecycle()
    val isSearched = viewModel.onClickSearch.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showBottomSheet.collectAsStateWithLifecycle()
    val editDeepLink by viewModel.editDeepLink.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val qrDialogState by viewModel.qrDialogState.collectAsState()

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

    SimpleAcquirePermissions(
        permissions = appPermissions,
        onGrant = {},
    )

    MainScreen(
        schemeList = schemeList,
        favoriteItems = items.first,
        generalItems = items.second,
        categories = categories,
        selectedCategory = selectedCategory,
        onCategorySelected = viewModel::updateSelectedCategory,
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
        onShowNotification = viewModel::showNotification,
        onGenerateQrCode = viewModel::generateQrCode,
        recordDeepLinkUsage = viewModel::recordDeepLinkUsage,
        onNavigateStats = onNavigateStats,
    )

    if (showBottomSheet != DeepLink.EMPTY) {
        DeepLinkBottomSheet(
            deepLink = showBottomSheet,
            onDismiss = viewModel::hideBottomSheet,
            onDelete = viewModel::deleteDeepLinkUrl,
            onEdit = viewModel::onEditDeepLink,
        )
    }

    editDeepLink?.let { deepLinkToEdit ->
        DeepLinkEditDialog(
            deepLinkToEdit = deepLinkToEdit,
            onSave = { updatedDeepLink ->
                viewModel.updateDeepLinkUrl(updatedDeepLink)
                viewModel.clearEditDeepLink()
                viewModel.hideBottomSheet()
            },
            onDismiss = viewModel::clearEditDeepLink,
        )
    }

    QrDialog(
        qrDialogState = qrDialogState,
        onDismiss = viewModel::hideQrDialog,
        onSaveImage = { bitmap ->
            context.saveQrBitmapToGallery(bitmap)
        },
    )
}

@Composable
fun MainScreen(
    favoriteItems: List<DeepLink>,
    generalItems: List<DeepLink>,
    schemeList: List<String>,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onSearch: (String, String, String) -> Unit,
    onUpdate: (DeepLink) -> Unit,
    onDelete: (DeepLink) -> Unit,
    onItemLongClick: (DeepLink) -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSearch: () -> Unit,
    onRegister: (String) -> Unit,
    onNavigateSaastatus: () -> Unit,
    onNavigateOgTagPreview: () -> Unit,
    onDeleteScheme: (String) -> Unit,
    onShowNotification: (Int, String, String, String) -> Unit,
    onGenerateQrCode: (DeepLink) -> Unit,
    recordDeepLinkUsage: (DeepLink) -> Unit,
    onNavigateStats: () -> Unit,
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
                    onNavigateSaastatus = onNavigateSaastatus,
                )
            }
        },
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 검색 아이콘
                IconButton(
                    onClick = onNavigateSearch,
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "스킴 검색",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.width(space4))
                // 통계보기 버튼
                Button(onClick = onNavigateStats) {
                    Text("통계 보기")
                }
            }
            DeepLinkSection(
                favoriteItems = favoriteItems,
                generalItems = generalItems,
                schemeList = schemeList,
                onSearch = onSearch,
                onUpdate = onUpdate,
                onDelete = onDelete,
                onItemLongClick = onItemLongClick,
                onRegister = onRegister,
                onDeleteScheme = onDeleteScheme,
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
                onShowNotification = onShowNotification,
                onGenerateQrCode = onGenerateQrCode,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                listState = listState,
                recordDeepLinkUsage = recordDeepLinkUsage,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HorizontalFloatingToolbarSection(
    onNavigateOgTagPreview: () -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSaastatus: () -> Unit,
) {
    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }

    HorizontalFloatingToolbar(
        expanded = isExpanded,
    ) {
        TooltipIcon(
            tooltipText = "OG Tag Preview",
            content = {
                IconButton(
                    onClick = onNavigateOgTagPreview,
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "OG Tag Preview",
                    )
                }
            },
        )
        TooltipIcon(
            tooltipText = "QR Code Scanner",
            content = {
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
            },
        )
        TooltipIcon(
            tooltipText = "Navigate Saastatus",
            content = {
                IconButton(
                    onClick = onNavigateSaastatus,
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "navigate saastatus",
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TooltipIcon(
    tooltipText: String,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
        tooltip = {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small,
                tonalElevation = 4.dp,
            ) {
                Text(
                    text = tooltipText,
                    modifier = Modifier.padding(horizontal = space8, vertical = space4),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        state = rememberTooltipState(),
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeepLinkBottomSheet(
    deepLink: DeepLink,
    onDismiss: () -> Unit,
    onDelete: (DeepLink) -> Unit,
    onEdit: (DeepLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    LaunchedEffect(Unit) {
        sheetState.show()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(horizontal = space16, vertical = space8),
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            val context = LocalContext.current
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
            ) {
                Column(
                    modifier = Modifier.padding(space12),
                ) {
                    deepLink.imageUrl.takeIf { it.isNotEmpty() }?.let { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(MaterialTheme.shapes.medium),
                        )
                        Spacer(modifier = Modifier.height(space12))
                    }

                    Text(
                        text = deepLink.title.takeIf { it.isNotEmpty() } ?: "제목 없음",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(space4))
                    Text(
                        text = deepLink.url,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            if (URLUtil.isValidUrl(deepLink.url)) {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        deepLink.url.toUri(),
                                    ),
                                )
                            } else {
                                Toast.makeText(context, "유효하지 않은 URL입니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (deepLink.isBookMarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "즐겨찾기",
                        tint = if (deepLink.isBookMarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(space24),
                    )
                    Spacer(modifier = Modifier.width(space8))
                    Text(
                        text = if (deepLink.isBookMarked) "즐겨찾기 추가됨" else "즐겨찾기 아님",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                val formattedTimestamp = remember(deepLink.timestamp) {
                    val instant = Instant.fromEpochMilliseconds(deepLink.timestamp)
                    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                    "${dateTime.year}년 ${dateTime.monthNumber}월 ${dateTime.dayOfMonth}일 ${
                        String.format(
                            "%02d",
                            dateTime.hour,
                        )
                    }:${String.format("%02d", dateTime.minute)}"
                }
                Text(
                    text = "생성일: $formattedTimestamp",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }

            if (deepLink.category.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "카테고리: ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = deepLink.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    onClick = { onEdit(deepLink) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("편집")
                }
                Spacer(modifier = Modifier.width(space16))
                TextButton(
                    onClick = {
                        onDelete(deepLink)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Text("삭제")
                }
            }
        }
    }
}

@Composable
private fun DeepLinkEditDialog(
    deepLinkToEdit: DeepLink,
    onSave: (DeepLink) -> Unit,
    onDismiss: () -> Unit,
) {
    var editedTitle by remember { mutableStateOf(deepLinkToEdit.title) }
    var editedUrl by remember { mutableStateOf(deepLinkToEdit.url) }
    var editedCategory by remember { mutableStateOf(deepLinkToEdit.category) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("딥링크 편집") },
        text = {
            Column {
                TextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { Text("제목") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(space8))
                TextField(
                    value = editedUrl,
                    onValueChange = { editedUrl = it },
                    label = { Text("URL") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(space8))
                TextField(
                    value = editedCategory,
                    onValueChange = { editedCategory = it },
                    label = { Text("카테고리") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        deepLinkToEdit.copy(
                            title = editedTitle,
                            url = editedUrl,
                            category = editedCategory,
                        ),
                    )
                },
            ) {
                Text("저장")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        },
    )
}

@Preview
@Composable
private fun PreviewMainScreen() {
    MainScreen(
        schemeList = listOf("http", "https"),
        onSearch = { _, _, _ -> },
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
        categories = listOf("Category1", "Category2"),
        selectedCategory = "Category1",
        onCategorySelected = { },
        onShowNotification = { _, _, _, _ -> },
        onGenerateQrCode = { },
        recordDeepLinkUsage = {},
        onNavigateStats = {},
    )
}

@Composable
fun QrDialog(
    qrDialogState: QrDialogState,
    onDismiss: () -> Unit,
    onSaveImage: (Bitmap) -> Unit,
) {
    when (qrDialogState) {
        is QrDialogState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LoadingIndicator()
            }
        }

        is QrDialogState.Success -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                text = {
                    Image(
                        bitmap = qrDialogState.bitmap.asImageBitmap(),
                        contentDescription = "QR 코드",
                    )
                },
                confirmButton = {
                    Button(onClick = { onSaveImage(qrDialogState.bitmap) }) {
                        Text("이미지 저장")
                    }
                },
                dismissButton = {
                    Button(onClick = onDismiss) { Text("닫기") }
                },
            )
        }

        is QrDialogState.Error -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("에러") },
                text = { Text(qrDialogState.message) },
                confirmButton = {},
                dismissButton = {
                    Button(onClick = onDismiss) { Text("닫기") }
                },
            )
        }

        QrDialogState.Hidden -> Unit
    }
}
