package com.keelim.scheme

import android.content.Context
import android.content.Intent
import android.net.Uri


enum class DeepLinkInfo(val host: String) {

    MAIN("main") {
        override fun getIntent(context: Context, deepLinkUri: Uri) =
            getMainIntent(context)
    },

    DETAIL("detail") {
        override fun getIntent(context: Context, deepLinkUri: Uri) =
            IdDetailActivity.Companion.getIntent(context, deepLinkUri)
    },

    COUNTRY("country") {
        override fun getIntent(context: Context, deepLinkUri: Uri) =
            IdActivity.Companion.getIntent(context, deepLinkUri)
    };

    abstract fun getIntent(context: Context, deepLinkUri: Uri): Intent

    companion object {
        fun getMainIntent(context: Context) = SchemeTestActivity.getIntent(context)

        operator fun invoke(uri: Uri): DeepLinkInfo = entries.find { it.host == uri.host } ?: MAIN
    }
}
