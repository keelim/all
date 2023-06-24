package com.keelim.cnubus.ui.screen.stations

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.keelim.composeutil.setThemeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeContent {
        }
    }
}
