@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.arducon.ui

import android.content.Intent
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
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.keelim.arducon.ui.screen.deeplink.CreateDeepLinkRoute
import com.keelim.arducon.ui.screen.main.MainRoute
import com.keelim.arducon.ui.screen.ogtag.OgTagPreviewRoute
import com.keelim.arducon.ui.screen.qr.QrRoute
import com.keelim.arducon.ui.screen.saastatus.main.SaastatusRoute
import com.keelim.arducon.ui.screen.search.SearchRoute
import com.keelim.arducon.ui.screen.stats.StatsScreen
import com.keelim.commonAndroid.ui.AppViewModel
import com.keelim.composeutil.AppState
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.ArduconRoute
import com.keelim.core.navigation.SaastatusRoute
import com.keelim.web.navigateToWebModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ArduConHost(
    appState: AppState,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val backStack = rememberMutableStateListOf<ArduconRoute>(ArduconRoute.Main)
    val motionScheme = MaterialTheme.motionScheme

    NavDisplay(
        modifier = modifier,
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
            entry<ArduconRoute.Main> {
                MainRoute(
                    onShowMessage = { message ->
                        coroutineScope.launch {
                            onShowSnackbar(message, null)
                        }
                    },
                    onQrCodeClick = {
                        backStack.add(ArduconRoute.Qr)
                    },
                    onNavigateSearch = {
                        backStack.add(ArduconRoute.Search)
                    },
                    onNavigateSaastatus = {
                        backStack.add(SaastatusRoute.Main)
                    },
                    onNavigateOgTagPreview = {
                        backStack.add(ArduconRoute.OgTagPreview)
                    },
                    onNavigateStats = {
                        backStack.add(ArduconRoute.Stats)
                    },
                )
            }
            entry<ArduconRoute.Qr> {
                QrRoute(
                    onShowBarcode = { barcode ->
                        coroutineScope.launch {
                            if (onShowSnackbar(barcode, ">")) {
                                Intent(
                                    Intent.ACTION_VIEW,
                                    barcode.toUri(),
                                ).let { context.startActivity(it) }
                            }
                        }
                    },
                )
            }
            entry<ArduconRoute.Search> {
                SearchRoute(
                    onUpdate = {
                        coroutineScope.launch {
                            onShowSnackbar("스킴 검색 화면이 로드되었습니다.", null)
                        }
                    },
                    onNavigateToCreateDeepLink = { scheme ->
                        backStack.add(ArduconRoute.CreateDeepLink(scheme))
                    },
                )
            }
            entry<ArduconRoute.CreateDeepLink> { route ->
                CreateDeepLinkRoute(
                    scheme = route.scheme,
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    onShowMessage = { message ->
                        coroutineScope.launch {
                            onShowSnackbar(message, null)
                        }
                    },
                )
            }
            entry<ArduconRoute.OgTagPreview> {
                OgTagPreviewRoute(
                    onNavigateToBrowser = { url ->
                        context.navigateToWebModule(url.toUri())
                    },
                )
            }
            entry<SaastatusRoute.Main> {
                SaastatusRoute(
                    onRegister = { backStack.add(SaastatusRoute.Search) },
                )
            }
            entry<ArduconRoute.Stats> {
                StatsScreen()
            }
        },
    )
}
