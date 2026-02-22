@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.composeutil.ui.theme

import android.app.Activity
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.keelim.core.designsystem.theme.DarkColorScheme
import com.keelim.core.designsystem.theme.KeelimTypography
import com.keelim.core.designsystem.theme.LightColorScheme

private const val IN_APP_UPDATE = 10

@Composable
fun KeelimTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> if (isDarkTheme) DarkColorScheme else LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb() // change color status bar here
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        val currentActivity = activity ?: return@LifecycleEventEffect
        val updateManager = AppUpdateManagerFactory.create(context)
        val updateInfo = updateManager.appUpdateInfo
        val immediateUpdateOptions =
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                .setAllowAssetPackDeletion(true)
                .build()

        updateInfo.addOnSuccessListener { info ->
            when (info.updateAvailability()) {
                UpdateAvailability.UPDATE_AVAILABLE ->
                    if (info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        updateManager.startUpdateFlowForResult(
                            info,
                            currentActivity,
                            immediateUpdateOptions,
                            IN_APP_UPDATE,
                        )
                    }
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                    updateManager.startUpdateFlowForResult(
                        info,
                        currentActivity,
                        immediateUpdateOptions,
                        IN_APP_UPDATE,
                    )
                }
                else -> Unit
            }
        }
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = KeelimTypography,
        content = content,
    )
}
