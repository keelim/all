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
package com.keelim.nandadiagnosis

import android.app.Application
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.MobileAds
import com.keelim.nandadiagnosis.di.downloadModule
import com.keelim.nandadiagnosis.utils.AppOpenManager
import com.keelim.nandadiagnosis.utils.ThemeHelper
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {
  private lateinit var appOpenManager: AppOpenManager
  override fun onCreate() {
    super.onCreate()

    MobileAds.initialize(this) {}
    appOpenManager = AppOpenManager(this) // 콜드 부팅에서 복귀시 ad

    startKoin {
      androidLogger(Level.NONE)
      androidContext(this@MyApplication)
      modules(downloadModule)
    }

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val themePref = sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE)
    ThemeHelper.applyTheme(themePref!!)
  }
}
