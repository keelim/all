package com.keelim.nandadiagnosis.ui.screen.category

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val categoryRoute = "category"
fun NavController.navigateToCategory(navOptions: NavOptions? = null) {
    this.navigate(categoryRoute, navOptions)
}

fun NavGraphBuilder.categoryScreen(
    onCategoryClick: (Int) -> Unit,
) {
    composable(route = categoryRoute) { CategoryRoute(onCategoryClick) }
}
