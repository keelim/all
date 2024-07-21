package com.keelim.nandadiagnosis.ui.screen.category

import androidx.compose.material3.SheetState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keelim.core.navigation.NandaRoute
import com.keelim.nandadiagnosis.ui.screen.main.MainBottomSheet

fun NavController.navigateToCategory() {
    this.navigate(NandaRoute.Category) {
        popUpTo(graph.id) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.categoryScreen(
    bottomSheetState: SheetState,
    onBlogClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDismiss: () -> Unit,
    onCategoryClick: (Int, String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<NandaRoute.Category> {
        CategoryRoute(
            onCategoryClick = onCategoryClick,
        )
        if (bottomSheetState.isVisible) {
            MainBottomSheet(
                onBlogClick = onBlogClick,
                onAboutClick = onAboutClick,
                onDismiss = onDismiss,
                modalBottomSheetState = bottomSheetState,
            )
        }
    }
    nestedGraphs()
}
