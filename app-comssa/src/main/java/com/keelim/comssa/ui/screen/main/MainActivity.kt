@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.keelim.comssa.ui.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.keelim.composeutil.ui.theme.KeelimTheme
import com.keelim.shared.data.UserStateStore
import com.keelim.shared.data.model.ThemeType
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userStateStore: Lazy<UserStateStore>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val themeType = userStateStore.get().themeTypeFlow.collectAsStateWithLifecycle(ThemeType.LIGHT).value
            val isDarkThem = when (themeType) {
                ThemeType.DARK -> true
                ThemeType.LIGHT -> false
            }
            KeelimTheme(
                isDarkTheme = isDarkThem,
            ) {
                ComssaApp(
                    windowSizeClass = calculateWindowSizeClass(this),
                )
            }
            LifecycleEventEffect(
                event = Lifecycle.Event.ON_CREATE,
            ) {
                updateVisitedTime()
            }
        }
    }

    private fun updateVisitedTime() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                userStateStore.get().updateVisitedTime()
            }
        }
    }
}
