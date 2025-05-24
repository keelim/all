@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.cnubus.ui

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.keelim.cnubus.ui.screen.main.MainRoute
import com.keelim.cnubus.ui.screen.map.screen.map.MapRoute
import com.keelim.composeutil.AppState
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.CnuBusRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.settings.settingsEntry
import kotlinx.coroutines.CoroutineScope

@Composable
fun CnubusHost(
    appState: AppState,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val backStack = rememberMutableStateListOf<AppRoute>(CnuBusRoute.Main)
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
            entry<CnuBusRoute.Main> {
                MainRoute(
                    onNavigateMap = {
                        backStack.add(CnuBusRoute.Map)
                    },
                    onNavigateAppSetting = {
                        backStack.add(FeatureRoute.Settings)
                    },
                )
            }
            entry<CnuBusRoute.Map> {
                MapRoute()
            }
            settingsEntry(
                backStack = backStack,
                context = context,
            )
        }
    )
}
