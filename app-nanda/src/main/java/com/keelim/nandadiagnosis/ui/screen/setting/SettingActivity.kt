package com.keelim.nandadiagnosis.ui.screen.setting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.keelim.composeutil.screen.setting.SettingScreen

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingScreen()
        }
    }
}
