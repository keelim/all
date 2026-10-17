@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.cnubus.ui

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.composeutil.navigation.KeelimNavDisplay
import com.keelim.cnubus.ui.screen.main.MainRoute
import com.keelim.cnubus.ui.screen.map.screen.map.MapRoute
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.CnuBusRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.navigation.registerSettingsEntries
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope

@Composable
fun CnubusHost(
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    backStack: SnapshotStateList<AppRoute> = rememberMutableStateListOf<AppRoute>(CnuBusRoute.Main),
) {
    val context = LocalContext.current

    KeelimNavDisplay(
        modifier = modifier,
        backStack = backStack,
    ) {
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
            registerSettingsEntries(
                backStack = backStack,
                context = context,
                onOpenSourceClick = {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                },
            )
    }
}
