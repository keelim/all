@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.keelim.mygrade.ui.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.os.BuildCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.keelim.commonAndroid.core.AppMainDelegator
import com.keelim.commonAndroid.core.AppMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@BuildCompat.PrereleaseSdkCheck
@AndroidEntryPoint
class CenterActivity : AppCompatActivity() {
    private val viewModel: AppMainViewModel by viewModels()
    private val appMainDelegator by lazy { AppMainDelegator(this, viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MyGradeApp(
                    windowSizeClass = calculateWindowSizeClass(this),
                )
            }
        }
    }
}
