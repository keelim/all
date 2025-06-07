@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.arducon.ui.screen.main

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.arducon.ui.component.AdBannerView
import com.keelim.composeutil.component.icon.rememberQrCodeScanner
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.model.DeepLink

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

    val context = LocalContext.current
    LaunchedEffect(isSearched.value) {
        if (isSearched.value.isEmpty()) return@LaunchedEffect
        try {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(isSearched.value),
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
        onQrCodeClick = onQrCodeClick,
        onNavigateSearch = onNavigateSearch,
        onRegister = viewModel::onRegister,
        onNavigateSaastatus = onNavigateSaastatus,
        onNavigateOgTagPreview = onNavigateOgTagPreview,
        onDeleteScheme = viewModel::deleteScheme,
    )
}

@Composable
fun MainScreen(
    favoriteItems: List<DeepLink>,
    generalItems: List<DeepLink>,
    schemeList: List<String>,
    onSearch: (String, String) -> Unit,
    onUpdate: (DeepLink) -> Unit,
    onDelete: (DeepLink) -> Unit,
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
        bottomBar = {
            AdBannerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = space8),
            )
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
    )
}
