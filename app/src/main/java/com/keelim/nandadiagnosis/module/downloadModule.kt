    package com.keelim.nandadiagnosis.module

    import android.app.DownloadManager
    import android.net.Uri
    import org.koin.dsl.module
    import java.io.File

    val downloadModule = module{
        single{
            (url:String, file: File) ->
            DownloadManager.Request(Uri.parse(url))
                    .setTitle("Downloading")
                    .setDescription("Downloading Database file")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)
        }
    }