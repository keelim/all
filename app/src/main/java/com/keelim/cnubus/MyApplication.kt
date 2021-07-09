/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
import com.google.android.gms.ads.MobileAds
import com.keelim.cnubus.data.repository.theme.ThemeRepository
import com.keelim.cnubus.utils.AppOpenManager
import com.keelim.cnubus.utils.ComponentLogger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var themeRepository: ThemeRepository

    @Inject
    lateinit var componentLogger: ComponentLogger

    private lateinit var appOpenManager: AppOpenManager
    private val appCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this) {}

        appOpenManager = AppOpenManager(this) // 콜드 부팅에서 복귀시 ad

        componentLogger.initialize(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appCoroutineScope.launch {
            AppCompatDelegate.setDefaultNightMode(
                themeRepository.getUserTheme().firstOrNull() ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )

        }
    }
}
