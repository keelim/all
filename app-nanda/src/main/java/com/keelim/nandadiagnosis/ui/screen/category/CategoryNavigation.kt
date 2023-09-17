package com.keelim.nandadiagnosis.ui.screen.category

import androidx.compose.material3.SheetState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.nandadiagnosis.ui.screen.main.MainBottomSheet

const val categoryRoute = "category"
fun NavController.navigateToCategory(navOptions: NavOptions? = null) {
    this.navigate(categoryRoute, navOptions)
}

fun NavGraphBuilder.categoryScreen(
    bottomSheetState: SheetState,
    onBlogClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDismiss: () -> Unit,
    onCategoryClick: (Int) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = categoryRoute) {
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
