package com.keelim.setting.screen.faq

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateFaq(navOptions: NavOptions? = null) {
    this.navigate(FeatureRoute.Faq, navOptions)
}

fun NavGraphBuilder.faqScreen(
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<FeatureRoute.Faq> {
        FaqRoute()
    }
    nestedGraphs()
}
