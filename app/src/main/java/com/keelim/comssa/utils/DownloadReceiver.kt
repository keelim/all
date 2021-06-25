package com.keelim.comssa.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.keelim.comssa.extensions.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadReceiver @Inject constructor(
  @ApplicationContext val context: Context
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