package com.keelim.data.di.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class DownloadReceiver(
    @ApplicationContext val context: Context
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent!!.action) {
            if (id == -1L) {
                val query = DownloadManager.Query().apply { setFilterById(id) }
                val downloadManager =
                    context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst().not()) return
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

                when (cursor.getInt(columnIndex)) {
                    DownloadManager.STATUS_SUCCESSFUL -> Unit
                    DownloadManager.STATUS_FAILED -> Unit
                    DownloadManager.STATUS_PAUSED -> Unit
                    else -> Unit
                }
            }
        }
    }
}
