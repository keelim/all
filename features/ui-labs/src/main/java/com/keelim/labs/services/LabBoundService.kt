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
package com.keelim.labs.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LabBoundService : Service() {
    private val binder = LocalBinder()
    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    fun getCurrentTime(): String {
        val current = LocalDateTime.now()
        return current.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    inner class LocalBinder : Binder() {
        fun getService(): LabBoundService {
            return this@LabBoundService
        }
    }
}
