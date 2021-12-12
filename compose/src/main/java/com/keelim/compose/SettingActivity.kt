package com.keelim.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingScreen { action ->
                when (action) {
                    ScreenAction.Content -> Unit
                    ScreenAction.Homepage -> Unit
                    ScreenAction.Map -> Unit
                    ScreenAction.Update -> Unit
                    ScreenAction.Theme -> Unit
                    ScreenAction.OpenSource -> Unit
                    ScreenAction.Lab -> Unit
                    ScreenAction.Developer -> Unit
                    ScreenAction.Subway -> Unit
                }
            }
        }
    }
}
