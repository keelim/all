package com.keelim.mygrade.ui.screen.setting

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.keelim.common.extensions.toast
import com.keelim.compose.screen.SettingAction
import com.keelim.compose.screen.SettingScreen
import com.keelim.mygrade.BuildConfig
import com.keelim.mygrade.R

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingScreen(
                modifier = Modifier,
                appName = getString(R.string.app_name),
                clickAction = ::handleAction,
                developerModeDataHolder = mapOf(
                    "appName" to getString(R.string.app_name),
                    "version" to BuildConfig.VERSION_NAME,
                    "build" to BuildConfig.VERSION_CODE.toString(),
                    "device_model" to Build.MODEL,
                    "device_os" to Build.VERSION.SDK_INT.toString(),
                ),
            )
        }
    }

    private fun handleAction(action: SettingAction) {
        when (action) {
            SettingAction.AlarmSetting -> {
            }
            SettingAction.Lab -> {
                toast("아직 진행 중인 실험 기능이 없습니다. ")
            }
            SettingAction.OtherApp -> {
            }
            SettingAction.ThemeSetting -> {
            }
            SettingAction.ShowLogcat -> TODO()
        }
    }
}
