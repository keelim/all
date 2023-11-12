package com.keelim.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.airbnb.android.showkase.models.Showkase
import com.keelim.composeutil.setThemeContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeContent {
            startActivity(Showkase.getBrowserIntent(this))
            finish()
        }
    }
}
