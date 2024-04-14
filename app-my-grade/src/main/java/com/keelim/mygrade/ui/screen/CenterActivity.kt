@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.keelim.mygrade.ui.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.os.BuildCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.airbnb.deeplinkdispatch.DeepLink
import com.keelim.composeutil.util.setThemeContent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@BuildCompat.PrereleaseSdkCheck
@DeepLink("all://screen/{name}")
@AndroidEntryPoint
class CenterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setThemeContent {
            MyGradeApp(
                windowSizeClass = calculateWindowSizeClass(this),
            )
        }
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            val parameters = intent.extras
            Timber.d("[deep link] name ${parameters?.getString("name")}")
        }
    }
}
