package com.keelim.nandadiagnosis.ui.screen.category

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
import kotlinx.collections.immutable.persistentListOf

private val appPermissions: List<String> = buildList {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
fun CategoryRoute(
    onCategoryClick: (Int, String) -> Unit,
    onEditTypeClick: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
) = trace("CategoryRoute") {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CategoryScreen(
        state = state,
        onCategoryClick = onCategoryClick,
        onEditTypeClick = onEditTypeClick,
    )
}

@Composable
fun CategoryScreen(
    state: CategoryState,
    onCategoryClick: (Int, String) -> Unit,
    onEditTypeClick: () -> Unit,
) = trace("CategoryScreen") {
    CategoryStateSection(
        uiState = state,
        onCategoryClick = onCategoryClick,
        onEditTypeClick = onEditTypeClick,
    )

    SimpleAcquirePermissions(
        permissions = appPermissions,
    ) { }
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
        onEditTypeClick = { },
    )
}
