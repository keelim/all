@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.keelim.comssa.ui.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.keelim.composeutil.setThemeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setThemeContent {
            ComssaApp(
                windowSizeClass = calculateWindowSizeClass(this),
            )
        }
    }
}
