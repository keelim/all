package com.keelim.mygrade.ui.screen.word

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute
import com.keelim.mygrade.ui.screen.word.show.WordShowRoute
import com.keelim.mygrade.ui.screen.word.write.WordWriteRoute

fun NavController.navigateWord(
    navOptions: NavOptions? = null,
) {
    this.navigate(MyGradeRoute.Word, navOptions)
}

fun NavController.navigateWordWrite(
    navOptions: NavOptions? = null,
) {
    this.navigate(MyGradeRoute.WordWrite, navOptions)
}

fun NavGraphBuilder.wordScreen(
    onWordWriteNavigate: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<MyGradeRoute.Word>{
        WordShowRoute(
            onWordWriteNavigate = onWordWriteNavigate,
        )
    }
    nestedGraphs()
}

fun NavGraphBuilder.wordWriteScreen() {
    composable<MyGradeRoute.WordWrite> {
        WordWriteRoute()
    }
}
