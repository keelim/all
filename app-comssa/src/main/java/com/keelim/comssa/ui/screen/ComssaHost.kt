@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.keelim.composeutil.AppState
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.comssa.ui.screen.main.ecocal.EcocalRoute
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.ComssaRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun ComssaHost(
    appState: AppState,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberMutableStateListOf<AppRoute>(ComssaRoute.Ecocal)
    val motionScheme = MaterialTheme.motionScheme

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                fadeOut(motionScheme.defaultEffectsSpec()),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                scaleOut(
                    targetScale = 0.7f,
                ),
            )
        },
        entryProvider = entryProvider {
            entry<ComssaRoute.Ecocal> {
                EcocalRoute()
            }
        },
    )
}
