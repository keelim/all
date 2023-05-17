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
package com.keelim.nandadiagnosis.di

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.keelim.common.extensions.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadReceiver @Inject constructor(
    @ApplicationContext val context: Context,
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent!!.action) {
            if (id == -1L) {
                val query = DownloadManager.Query().apply { setFilterById(id) }
                val downloadManager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst().not()) return
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

                when (cursor.getInt(columnIndex)) {
                    DownloadManager.STATUS_SUCCESSFUL ->
                        context.toast("다운로드가 완료되었습니다.")

                    DownloadManager.STATUS_FAILED ->
                        context.toast("다운로드가 실패되었습니다")

                    DownloadManager.STATUS_PAUSED ->
                        context.toast("다운로드가 중지되었습니다.")

                    else -> Unit
                }
            }
        }
    }
}
