@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.cnubus.ui

import android.content.Context
import android.content.Intent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.cnubus.ui.screen.main.MainRoute
import com.keelim.cnubus.ui.screen.map.screen.map.MapRoute
import com.keelim.composeutil.AppState
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.CnuBusRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.admin.AdminRoute
import com.keelim.setting.screen.alarm.AlarmRoute
import com.keelim.setting.screen.faq.FaqRoute
import com.keelim.setting.screen.lab.LabRoute
import com.keelim.setting.screen.notification.NotificationRoute
import com.keelim.setting.screen.settings.SettingsRoute
import com.keelim.setting.screen.theme.ThemeRoute
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
            settingsEntry(backStack, context)
        },
    )
}

@Composable
fun EntryProviderBuilder<Any>.settingsEntry(
    backStack: SnapshotStateList<Any>,
    context: Context
) {
    entry<FeatureRoute.Settings> {
        SettingsRoute(
            onThemeChangeClick = { backStack.add(FeatureRoute.Theme) },
            onNotificationsClick = {
                backStack.add(FeatureRoute.Notification)
            },
            onAlarmsClick = {
                backStack.add(FeatureRoute.Alarm)
            },
            onFaqClick = {
                backStack.add(FeatureRoute.Faq)
            },
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
            onLabClick = {
                backStack.add(FeatureRoute.Lab)
            },
            onAppUpdateClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://play.google.com/store/apps/details?id=${context.packageName}".toUri(),
                    ),
                )
            },
            onAdminClick = {
                backStack.add(FeatureRoute.Admin)
            },
        )
    }
    entry<FeatureRoute.Faq> {
        FaqRoute()
    }
    entry<FeatureRoute.Theme> {
        ThemeRoute()
    }
    entry<FeatureRoute.Notification> {
        NotificationRoute()
    }
    entry<FeatureRoute.Lab> {
        LabRoute()
    }
    entry<FeatureRoute.Alarm> {
        AlarmRoute()
    }
    entry<FeatureRoute.Admin> {
        AdminRoute()
    }
}

