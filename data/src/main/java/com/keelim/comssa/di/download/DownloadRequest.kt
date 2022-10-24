package com.keelim.comssa.di.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.keelim.comssa.data.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

class DownloadRequest(
    @ApplicationContext private val ctx: Context
) {
    fun provideDownloadRequest(link: String? = null): DownloadManager.Request {
        return DownloadManager.Request(Uri.parse(link ?: ctx.getString(R.string.db_path)))
            .setTitle("Downloading")
            .setDescription("Downloading Database file")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(File(ctx.getExternalFilesDir(null), "comssa.db")))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
    }
}
