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
import com.google.android.material.color.DynamicColors
import com.keelim.commonAndroid.util.ComponentLogger
import com.keelim.nandadiagnosis.notification.NotificationChannels
import com.keelim.nandadiagnosis.utils.AppOpenManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var componentLogger: ComponentLogger

    override fun onCreate() {
        super.onCreate()
        componentLogger.initialize(this)
        NotificationChannels.initialize(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
