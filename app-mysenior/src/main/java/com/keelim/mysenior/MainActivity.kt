@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.keelim.mysenior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.keelim.composeutil.ui.theme.KeelimTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            KeelimTheme {
                MySeniorApp(
                    windowSizeClass = calculateWindowSizeClass(this),
                )
            }
        }
    }
}
