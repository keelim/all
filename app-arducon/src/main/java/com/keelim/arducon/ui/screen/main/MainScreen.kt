@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.arducon.ui.screen.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            FloatingSection(
                onNavigateOgTagPreview = onNavigateOgTagPreview,
                onQrCodeClick = onQrCodeClick,
                onNavigateSearch = onNavigateSearch,
                onNavigateSaastatus = onNavigateSaastatus,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            verticalArrangement = Arrangement.spacedBy(space16),
        ) {
            MainTopSection(
                schemeList = schemeList,
                onSearch = onSearch,
                onRegister = onRegister,
                onDelete = onDeleteScheme,
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
            )
            DeepLinkSection(
                favoriteItems = favoriteItems,
                generalItems = generalItems,
                onUpdate = onUpdate,
                onDelete = onDelete,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun FloatingSection(
    onNavigateOgTagPreview: () -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateSaastatus: () -> Unit,
) {
    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }

    Box {
        FloatingActionButtonMenu(
            expanded = isExpanded,
            button = {
                ToggleFloatingActionButton(
                    checked = isExpanded,
                    onCheckedChange = setIsExpanded,
                    content = {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                            contentDescription = if (isExpanded) "Close" else "Open",
                        )
                    },
                )
            },
        ) {
            FloatingActionButtonMenuItem(
                onClick = onNavigateOgTagPreview,
                icon = {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "OG Tag Preview",
                    )
                },
                text = { Text("OG Tag Preview") },
            )
            FloatingActionButtonMenuItem(
                onClick = onQrCodeClick,
                icon = {
                    Icon(
                        imageVector = rememberQrCodeScanner(
                            tintColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        ),
                        contentDescription = "QR Code Scanner",
                    )
                },
                text = { Text("QR Code Scanner") },
            )
            FloatingActionButtonMenuItem(
                onClick = onNavigateSearch,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                    )
                },
                text = { Text("Search") },
            )
            FloatingActionButtonMenuItem(
                onClick = onNavigateSaastatus,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "navigate saastatus",
                    )
                },
                text = { Text("SaaStatus") },
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
