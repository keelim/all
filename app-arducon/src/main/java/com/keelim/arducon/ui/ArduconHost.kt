@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.arducon.ui

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keelim.composeutil.navigation.KeelimNavDisplay
import com.keelim.arducon.ui.screen.base64.Base64Screen
import com.keelim.arducon.ui.screen.deeplink.CreateDeepLinkRoute
import com.keelim.arducon.ui.screen.json.JsonFormatterScreen
import com.keelim.arducon.ui.screen.main.MainRoute
import com.keelim.arducon.ui.screen.ogtag.OgTagPreviewRoute
import com.keelim.arducon.ui.screen.playground.PlaygroundRoute
import com.keelim.arducon.ui.screen.qr.QrRoute
import com.keelim.arducon.ui.screen.saastatus.main.SaastatusRoute
import com.keelim.arducon.ui.screen.search.SearchRoute
import com.keelim.arducon.ui.screen.stats.StatsScreen
import com.keelim.arducon.ui.screen.urlshortener.UrlShortenerScreen
import com.keelim.commonAndroid.ui.AppViewModel
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.ArduconRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.core.navigation.SaastatusRoute
import com.keelim.setting.screen.device.DeviceInfoScreen
import com.keelim.web.navigateToWebModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ArduConHost(
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val backStack = rememberMutableStateListOf<AppRoute>(ArduconRoute.Main)

    KeelimNavDisplay(
        modifier = modifier,
        backStack = backStack,
    ) {
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
                    onNavigatePlayground = {
                        backStack.add(ArduconRoute.Playground)
                    },
                    onNavigateJsonFormatter = {
                        backStack.add(ArduconRoute.JsonFormatter)
                    },
                    onNavigateBase64Encoder = {
                        backStack.add(ArduconRoute.Base64Encoder)
                    },
                    onNavigateDeviceInfo = {
                        backStack.add(FeatureRoute.DeviceInfo)
                    },
                    onNavigateUrlShortener = {
                        backStack.add(ArduconRoute.UrlShortener)
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
            entry<ArduconRoute.Playground> {
                PlaygroundRoute(
                    onNavigateBack = { backStack.removeLastOrNull() },
                )
            }
            entry<ArduconRoute.JsonFormatter> {
                JsonFormatterScreen(
                    onNavigateBack = { backStack.removeLastOrNull() },
                )
            }
            entry<ArduconRoute.Base64Encoder> {
                Base64Screen(
                    onNavigateBack = { backStack.removeLastOrNull() },
                )
            }
            entry<FeatureRoute.DeviceInfo> {
                DeviceInfoScreen(
                    onNavigateBack = { backStack.removeLastOrNull() },
                )
            }
            entry<ArduconRoute.UrlShortener> {
                UrlShortenerScreen(
                    onNavigateBack = { backStack.removeLastOrNull() },
                )
            }
    }
}
