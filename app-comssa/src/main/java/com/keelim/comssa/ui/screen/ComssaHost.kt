@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen

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
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.comssa.ui.screen.main.calculator.CalculatorRoute
import com.keelim.comssa.ui.screen.main.ecocal.EcocalRoute
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.ComssaRoute
import com.keelim.setting.navigation.registerSettingsEntries
import kotlinx.coroutines.CoroutineScope

@Composable
fun ComssaHost(
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val backStack = rememberMutableStateListOf<AppRoute>(ComssaRoute.Ecocal)
    val motionScheme = MaterialTheme.motionScheme

    NavDisplay(
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
            entry<ComssaRoute.Ecocal> {
                EcocalRoute(
                    onNavigateToFinancialCalculators = {
                        backStack.add(ComssaRoute.FinancialCalculators)
                    },
                )
            }
            entry<ComssaRoute.FinancialCalculators> {
                CalculatorRoute()
            }
            registerSettingsEntries(
                backStack = backStack,
                context = context,
                onOpenSourceClick = {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                },
            )
        },
    )
}
