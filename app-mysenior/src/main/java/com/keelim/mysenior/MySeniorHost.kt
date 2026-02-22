@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.mysenior

import android.content.Intent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.keelim.composeutil.AppState
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
    val motionScheme = MaterialTheme.motionScheme

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
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
            registerSettingsEntries(
                backStack = backStack,
                context = context,
                onOpenSourceClick = {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                },
            )
            entry<FeatureRoute.Event> {
                EventRoute()
            },
        },
    )
}
