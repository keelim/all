@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.arducon.ui.screen.main

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.webkit.URLUtil
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import com.keelim.core.designsystem.component.KuiAlertDialog
import com.keelim.core.designsystem.component.KuiButton
import androidx.compose.material3.ButtonDefaults
import com.keelim.core.designsystem.component.KuiElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiLoadingIndicator
import com.keelim.core.designsystem.component.KuiModalBottomSheet
import com.keelim.core.designsystem.component.KuiOutlinedButton
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTextButton
import com.keelim.core.designsystem.component.KuiFilledTextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.keelim.arducon.ui.screen.main.MainViewModel.QrDialogState
import com.keelim.common.extensions.saveQrBitmapToGallery
import com.keelim.commonAndroid.extensions.toUiDateTime
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.resource.*
import com.keelim.model.DeepLink
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource

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
    onNavigatePlayground: () -> Unit,
    onNavigateJsonFormatter: () -> Unit,
    onNavigateBase64Encoder: () -> Unit,
    onNavigateDeviceInfo: () -> Unit,
    onNavigateUrlShortener: () -> Unit,
    onNavigateDeviceTestLab: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val schemeList by viewModel.schemeList.collectAsStateWithLifecycle()
    val items by viewModel.deepLinkList.collectAsStateWithLifecycle()
    val isSearched = viewModel.onClickSearch.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showBottomSheet.collectAsStateWithLifecycle()
    val editDeepLink by viewModel.editDeepLink.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val qrDialogState by viewModel.qrDialogState.collectAsStateWithLifecycle()

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
        onNavigatePlayground = onNavigatePlayground,
        onNavigateJsonFormatter = onNavigateJsonFormatter,
        onNavigateBase64Encoder = onNavigateBase64Encoder,
        onNavigateDeviceInfo = onNavigateDeviceInfo,
        onDeleteScheme = viewModel::deleteScheme,
        onShowNotification = viewModel::showNotification,
        onGenerateQrCode = viewModel::generateQrCode,
        recordDeepLinkUsage = viewModel::recordDeepLinkUsage,
        onNavigateStats = onNavigateStats,
        onNavigateUrlShortener = onNavigateUrlShortener,
        onNavigateDeviceTestLab = onNavigateDeviceTestLab,
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
    onNavigatePlayground: () -> Unit,
    onNavigateJsonFormatter: () -> Unit,
    onNavigateBase64Encoder: () -> Unit,
    onNavigateDeviceInfo: () -> Unit,
    onDeleteScheme: (String) -> Unit,
    onShowNotification: (Int, String, String, String) -> Unit,
    onGenerateQrCode: (DeepLink) -> Unit,
    recordDeepLinkUsage: (DeepLink) -> Unit,
    onNavigateStats: () -> Unit,
    onNavigateUrlShortener: () -> Unit,
    onNavigateDeviceTestLab: () -> Unit,
) {
    KuiScaffold(
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
                KuiText(
                    text = stringResource(Res.string.arducon_main_title),
                    style = KuiTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = KuiTheme.colorScheme.primary,
                )
                KuiText(
                    text = stringResource(Res.string.arducon_main_subtitle),
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    ) { paddingValues ->
        DeepLinkSection(
            favoriteItems = favoriteItems,
            generalItems = generalItems,
            schemeList = schemeList,
            onSearch = onSearch,
            onUpdate = onUpdate,
            onDelete = onDelete,
            onItemLongClick = onItemLongClick,
            onQrCodeClick = onQrCodeClick,
            onNavigateSearch = onNavigateSearch,
            onRegister = onRegister,
            onNavigateSaastatus = onNavigateSaastatus,
            onNavigateOgTagPreview = onNavigateOgTagPreview,
            onNavigatePlayground = onNavigatePlayground,
            onNavigateJsonFormatter = onNavigateJsonFormatter,
            onNavigateBase64Encoder = onNavigateBase64Encoder,
            onNavigateDeviceInfo = onNavigateDeviceInfo,
            onDeleteScheme = onDeleteScheme,
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
            onShowNotification = onShowNotification,
            onGenerateQrCode = onGenerateQrCode,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            recordDeepLinkUsage = recordDeepLinkUsage,
            onNavigateStats = onNavigateStats,
            onNavigateUrlShortener = onNavigateUrlShortener,
            onNavigateDeviceTestLab = onNavigateDeviceTestLab,
        )
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

    KuiModalBottomSheet(
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
            val invalidUrlError = stringResource(Res.string.invalid_url_error)
            KuiElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = KuiTheme.shapes.large,
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
                                .clip(KuiTheme.shapes.medium),
                        )
                        Spacer(modifier = Modifier.height(space12))
                    }

                    KuiText(
                        text = deepLink.title.takeIf { it.isNotEmpty() } ?: stringResource(Res.string.arducon_main_no_title),
                        style = KuiTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = KuiTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(space4))
                    KuiText(
                        text = deepLink.url,
                        style = KuiTheme.typography.bodyLarge,
                        color = KuiTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            if (URLUtil.isValidUrl(deepLink.url)) {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        deepLink.url.toUri(),
                        ),
                    )
                } else {
                    Toast.makeText(
                        context,
                        invalidUrlError,
                        Toast.LENGTH_SHORT
                    ).show()
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
                    KuiIcon(
                        imageVector = if (deepLink.isBookMarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(Res.string.arducon_main_favorite),
                        tint = if (deepLink.isBookMarked) KuiTheme.colorScheme.primary else KuiTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(space24),
                    )
                    Spacer(modifier = Modifier.width(space8))
                    KuiText(
                        text = if (deepLink.isBookMarked) stringResource(Res.string.arducon_main_favorite_added) else stringResource(Res.string.arducon_main_favorite_not_added),
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                }

                val formattedTimestamp = remember(deepLink.timestamp) {
                    Instant.fromEpochMilliseconds(deepLink.timestamp).toUiDateTime()
                }
                KuiText(
                    text = stringResource(Res.string.arducon_main_created_at, formattedTimestamp),
                    style = KuiTheme.typography.bodySmall,
                    color = KuiTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }

            if (deepLink.category.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KuiText(
                        text = stringResource(Res.string.arducon_main_category),
                        style = KuiTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                    KuiText(
                        text = deepLink.category,
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onSurface,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                KuiOutlinedButton(
                    onClick = { onEdit(deepLink) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    KuiText(stringResource(Res.string.common_action_edit))
                }
                Spacer(modifier = Modifier.width(space16))
                KuiTextButton(
                    onClick = {
                        onDelete(deepLink)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColors(contentColor = KuiTheme.colorScheme.error),
                ) {
                    KuiText(stringResource(Res.string.common_action_delete))
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

    KuiAlertDialog(
        onDismissRequest = onDismiss,
        title = { KuiText(stringResource(Res.string.arducon_main_edit_deeplink)) },
        text = {
            Column {
                KuiFilledTextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { KuiText(stringResource(Res.string.arducon_main_title_label)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(space8))
                KuiFilledTextField(
                    value = editedUrl,
                    onValueChange = { editedUrl = it },
                    label = { KuiText(stringResource(Res.string.label_url)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(space8))
                KuiFilledTextField(
                    value = editedCategory,
                    onValueChange = { editedCategory = it },
                    label = { KuiText(stringResource(Res.string.arducon_main_category_label)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            KuiButton(
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
                KuiText(stringResource(Res.string.common_action_save))
            }
        },
        dismissButton = {
            KuiButton(onClick = onDismiss) {
                KuiText(stringResource(Res.string.common_action_cancel))
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
        onNavigatePlayground = {},
        onNavigateJsonFormatter = {},
        onNavigateBase64Encoder = {},
        onNavigateDeviceInfo = {},
        onDeleteScheme = {},
        onItemLongClick = { },
        categories = listOf("Category1", "Category2"),
        selectedCategory = "Category1",
        onCategorySelected = { },
        onShowNotification = { _, _, _, _ -> },
        onGenerateQrCode = { },
        recordDeepLinkUsage = {},
        onNavigateStats = {},
        onNavigateUrlShortener = {},
        onNavigateDeviceTestLab = {},
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
                KuiLoadingIndicator()
            }
        }

        is QrDialogState.Success -> {
            KuiAlertDialog(
                onDismissRequest = onDismiss,
                text = {
                    Image(
                        bitmap = qrDialogState.bitmap.asImageBitmap(),
                        contentDescription = stringResource(Res.string.qr_content_description),
                    )
                },
                confirmButton = {
                    KuiButton(onClick = { onSaveImage(qrDialogState.bitmap) }) {
                        KuiText(stringResource(Res.string.qr_save_image))
                    }
                },
                dismissButton = {
                    KuiButton(onClick = onDismiss) { KuiText(stringResource(Res.string.dialog_close)) }
                },
            )
        }

        is QrDialogState.Error -> {
            KuiAlertDialog(
                onDismissRequest = onDismiss,
                title = { KuiText(stringResource(Res.string.dialog_error)) },
                text = { KuiText(qrDialogState.message) },
                confirmButton = {},
                dismissButton = {
                    KuiButton(onClick = onDismiss) { KuiText(stringResource(Res.string.dialog_close)) }
                },
            )
        }

        QrDialogState.Hidden -> Unit
    }
}
