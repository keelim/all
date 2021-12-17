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
import com.jakewharton.threetenabp.AndroidThreeTen
import com.keelim.cnubus.data.repository.theme.ThemeRepositoryImpl
import com.keelim.cnubus.utils.AppOpenManager
import com.keelim.cnubus.utils.ComponentLogger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var themeRepositoryImpl: ThemeRepositoryImpl
    @Inject
    lateinit var componentLogger: ComponentLogger

    private lateinit var appOpenManager: AppOpenManager
    private val appCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        appOpenManager = AppOpenManager(this) // 콜드 부팅에서 복귀시 ad
        componentLogger.initialize(this)
        AndroidThreeTen.init(this)

        appCoroutineScope.launch {
            AppCompatDelegate.setDefaultNightMode(
                themeRepositoryImpl.getUserTheme().firstOrNull() ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
    }
}
