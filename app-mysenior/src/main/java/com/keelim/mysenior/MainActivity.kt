@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.keelim.mysenior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.keelim.composeutil.setThemeContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setThemeContent {
            MySeniorApp(
                windowSizeClass = calculateWindowSizeClass(this),
            )
        }
    }
}
