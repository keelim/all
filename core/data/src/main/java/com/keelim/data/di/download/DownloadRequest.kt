package com.keelim.data.di.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

class DownloadRequest(
    @ApplicationContext private val ctx: Context,
) {
    fun provideDownloadRequest(link: String): DownloadManager.Request {
        return DownloadManager.Request(Uri.parse(link))
            .setTitle("Downloading")
            .setDescription("Downloading Database file")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(File(ctx.getExternalFilesDir(null), "comssa.db")))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
    }
}
