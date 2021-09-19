package com.keelim.ui_setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.common.extraNotNull
import com.keelim.compose.ui.setThemeContent
import com.keelim.ui_setting.ui.Navigation
import com.keelim.ui_setting.ui.Section
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private val type by lazy{
        (intent.extras?.get("type") as Section?) ?: Section.Developer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeContent {
            Navigation(
                path = type,
                onBackAction = { onBackPressed() }
            )
        }
    }
}