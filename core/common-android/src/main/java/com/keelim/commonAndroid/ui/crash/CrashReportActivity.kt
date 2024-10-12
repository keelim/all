package com.keelim.commonAndroid.ui.crash

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrashReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val errorMessage: String = intent.getStringExtra("error").toString()
    }
}
