package com.keelim.mygrade.ui.screen.word

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.mygrade.ui.screen.word.show.WordShowRoute
import com.keelim.mygrade.ui.screen.word.write.WordWriteRoute

const val WordRoute = "word"
const val WordWriteRoute=  "$WordRoute/write"

fun NavController.navigateWord(
    navOptions: NavOptions? = null,
) {
    this.navigate(WordRoute, navOptions)
}

fun NavController.navigateWordWrite(
    navOptions: NavOptions? = null
) {
    this.navigate(WordWriteRoute, navOptions)
}

fun NavGraphBuilder.wordScreen(
    onWordWriteNavigate: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(
        route = WordRoute
    ) {
        WordShowRoute(
            onWordWriteNavigate = onWordWriteNavigate
        )
    }
    nestedGraphs()
}

fun NavGraphBuilder.wordWriteScreen() {
    composable(
        route = WordWriteRoute
    ) {
        WordWriteRoute()
    }
}
