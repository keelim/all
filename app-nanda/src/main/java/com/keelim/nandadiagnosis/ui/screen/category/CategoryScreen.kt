@file:OptIn(ExperimentalLayoutApi::class)

package com.keelim.nandadiagnosis.ui.screen.category

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CategoryRoute(
    onCategoryClick: (Int, String) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
) = trace("CategoryRoute") {
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current,
    )
    CategoryScreen(
        state = state,
        onCategoryClick = onCategoryClick,
    )
}

@Composable
fun CategoryScreen(
    state: CategoryState,
    onCategoryClick: (Int, String) -> Unit,
) = trace("CategoryScreen") {
    CategoryStateSection(uiState = state, onCategoryClick = onCategoryClick)
}

@Preview(showBackground = true)
@Composable
private fun PreviewCategoryScreen() {
    CategoryScreen(
        state = CategoryState.Success(
            items = persistentListOf(
                "a",
            ),
        ),
        onCategoryClick = { _, _ -> },
    )
}
