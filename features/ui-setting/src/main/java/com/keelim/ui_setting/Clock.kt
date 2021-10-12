package com.keelim.ui_setting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.keelim.compose.ui.setThemeContent
import com.keelim.ui_setting.ui.ClockScreen


class ClockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeContent {
            MaterialTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    ClockScreen()
                }
            }
        }
    }
}


