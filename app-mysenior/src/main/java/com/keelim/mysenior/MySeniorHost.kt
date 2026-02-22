@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.mysenior

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.keelim.composeutil.AppState
import com.keelim.composeutil.navigation.KeelimNavDisplay
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.navigation.registerSettingsEntries
import com.keelim.setting.screen.event.EventRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun MySeniorHost(
    appState: AppState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val backStack = rememberMutableStateListOf<AppRoute>(FeatureRoute.Settings)

    KeelimNavDisplay(
        modifier = modifier,
        backStack = backStack,
    ) {
            registerSettingsEntries(
                backStack = backStack,
                context = context,
                onOpenSourceClick = {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                },
            )
            entry<FeatureRoute.Event> {
                EventRoute()
            }
    }
}
