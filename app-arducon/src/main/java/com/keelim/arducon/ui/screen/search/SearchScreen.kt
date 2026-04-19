package com.keelim.arducon.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space16
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchRoute(
    onUpdate: () -> Unit,
    onNavigateToCreateDeepLink: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredSchemes by viewModel.filteredSchemes.collectAsStateWithLifecycle()

    SearchScreen(
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onClearSearch = viewModel::clearSearch,
        filteredSchemes = filteredSchemes,
        onSchemeClick = { scheme ->
            onNavigateToCreateDeepLink(scheme)
        },
    )

    LaunchedEffect(Unit) {
        onUpdate()
    }
}

@Composable
fun SearchScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    filteredSchemes: List<String>,
    onSchemeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.arducon_search_title),
                        style = KuiTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = space16),
            verticalArrangement = Arrangement.spacedBy(space16),
        ) {
            // 검색 입력 필드
            SearchInputField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                onClear = onClearSearch,
                modifier = Modifier.fillMaxWidth(),
            )

            // 검색 결과
            SchemeSearchSection(
                schemes = filteredSchemes,
                onSchemeClick = onSchemeClick,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SearchInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = KuiTheme.shapes.medium,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(Res.string.arducon_search_title)) },
            placeholder = { Text(stringResource(Res.string.arducon_search_placeholder)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(Res.string.common_action_search),
                    tint = KuiTheme.colorScheme.primary,
                )
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Res.string.arducon_search_clear_query),
                            tint = KuiTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            singleLine = true,
            shape = KuiTheme.shapes.small,
        )
    }
}

@Preview
@Composable
private fun PreviewSearchScreen() {
    SearchScreen(
        searchQuery = "",
        onSearchQueryChange = {},
        onClearSearch = {},
        filteredSchemes = listOf("http", "https", "tel", "mailto"),
        onSchemeClick = {},
    )
}
