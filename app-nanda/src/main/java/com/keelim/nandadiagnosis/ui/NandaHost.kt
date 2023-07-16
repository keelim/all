package com.keelim.nandadiagnosis.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.keelim.nandadiagnosis.ui.screen.main.category.categoryRoute
import com.keelim.nandadiagnosis.ui.screen.main.category.categoryScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun NandaHost(
    appState: NandaState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = categoryRoute,
        modifier = modifier,
    ) {
        categoryScreen(
            onCategoryClick = { }
        )
    }
}
