package com.keelim.ui_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import com.keelim.common.startActivity
import com.keelim.compose.ui.setThemeContent
import com.keelim.ui_setting.ui.Section
import com.keelim.ui_setting.ui.SettingScreen
import okhttp3.Route

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return setThemeContent {
            val context = LocalContext.current

            SettingScreen { action ->
                val route = when(action){
                    Section.Developer -> Section.Developer
                }

                context.startActivity<SettingActivity>(
                    "type" to route
                )
            }
        }
    }
}