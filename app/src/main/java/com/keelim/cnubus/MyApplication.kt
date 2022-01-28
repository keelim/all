/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.keelim.cnubus.data.repository.theme.ThemeRepository
import com.keelim.cnubus.utils.AppOpenManager
import com.keelim.cnubus.utils.ComponentLogger
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var themeRepository: ThemeRepository
    @Inject
    lateinit var componentLogger: ComponentLogger
    @Inject
    lateinit var appOpenManager: AppOpenManager
    private val scope by lazy { MainScope() }

    override fun onCreate() {
        super.onCreate()
        appOpenManager.initialize(this)
        componentLogger.initialize(this)

        scope.launch {
            AppCompatDelegate.setDefaultNightMode(
                themeRepository.getUserTheme().firstOrNull()
                    ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
        AppCenter.start(
            this,
            BuildConfig.APPCENTER_KEY,
            Analytics::class.java,
            Crashes::class.java
        )
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
