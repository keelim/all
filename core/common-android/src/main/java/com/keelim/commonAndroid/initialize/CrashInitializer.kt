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
package com.keelim.commonAndroid.initialize

import android.content.Context
import android.content.Intent
import androidx.startup.Initializer
import com.keelim.commonAndroid.ui.crash.CrashReportActivity

class CrashInitializer : Initializer<Unit>, Thread.UncaughtExceptionHandler {

    private lateinit var crashHandleContext: Context

    override fun create(context: Context) {
        crashHandleContext = context
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun dependencies() = listOf(TimberInitializer::class.java)

    override fun uncaughtException(t: Thread, e: Throwable) {
        crashHandleContext.startActivity(
            Intent(
                crashHandleContext,
                CrashReportActivity::class.java,
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("error", e.stackTraceToString())
            },
        )
    }
}
