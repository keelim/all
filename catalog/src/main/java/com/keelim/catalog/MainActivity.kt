package com.keelim.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.airbnb.android.showkase.models.Showkase
import com.keelim.composeutil.ui.theme.KeelimTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeelimTheme {
                startActivity(Showkase.getBrowserIntent(this))
                finish()
            }
        }
    }
}
