package com.keelim.arducon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.keelim.arducon.ui.ArduconApp
import com.keelim.composeutil.setThemeContent
import com.keelim.shared.data.UserStateStore
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userStateStore: Lazy<UserStateStore>

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setThemeContent(userStateStore.get()) { windowSizeClass ->
            ArduconApp(
                windowSizeClass = windowSizeClass,
            )
        }
    }
}
