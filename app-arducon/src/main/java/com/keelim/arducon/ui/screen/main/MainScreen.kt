package com.keelim.arducon.ui.screen.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.arducon.ui.component.AdBannerView
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space8
import com.keelim.core.database.model.DeepLink

@Composable
fun MainRoute(
    onShowMessage: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val items by viewModel.deepLinkList.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )
    val isSearched = viewModel.onClickSearch.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )

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
        favoriteItems = items.first,
        generalItems = items.second,
        onSearch = viewModel::onClickSearch,
        onUpdate = viewModel::updateDeepLinkUrl,
        onDelete = viewModel::deleteDeepLinkUrl,
    )
}

@Composable
fun MainScreen(
    favoriteItems: List<DeepLink>,
    generalItems: List<DeepLink>,
    onSearch: (String, String) -> Unit,
    onUpdate: (DeepLink) -> Unit,
    onDelete: (DeepLink) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = space16, vertical = space12),
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        Text(
            text = "Arducon Deeplink Tester",
            style = MaterialTheme.typography.titleLarge,
        )
        MainTopSection(
            onSearch = onSearch,
        )
        HorizontalDivider()
        DeepLinkSection(
            favoriteItems = favoriteItems,
            generalItems = generalItems,
            onUpdate = onUpdate,
            onDelete = onDelete,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )
        AdBannerView(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    MainScreen(
        onSearch = {_, _ ->},
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
    )
}
