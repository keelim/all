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
package com.keelim.nandadiagnosis.ui.screen.main

import android.Manifest
import android.app.DownloadManager
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.airbnb.deeplinkdispatch.DeepLink
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keelim.common.extensions.toast
import com.keelim.commonAndroid.core.AppMainDelegator
import com.keelim.commonAndroid.core.AppMainViewModel
import com.keelim.composeutil.setThemeContent
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.di.DownloadReceiver
import com.keelim.nandadiagnosis.ui.screen.NandaApp
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@DeepLink("all://screen/{name}")
@AndroidEntryPoint
class Main2Activity : ComponentActivity() {
    private val viewModel: AppMainViewModel by viewModels()
    private val appMainDelegator by lazy { AppMainDelegator(this, viewModel) }

    @Inject
    lateinit var receiver: DownloadReceiver

    private val appPermissions: List<String> = buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter { appPermissions.contains(it.key) }
            if (responsePermissions.filter { it.value }.size == appPermissions.size) {
                toast("권한이 확인되었습니다.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setThemeContent {
            NandaApp(
                windowSizeClass = calculateWindowSizeClass(this),
            )
        }
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            val parameters = intent.extras
            Timber.d("[deep link] name ${parameters?.getString("name")}")
        }
    }

    override fun onStart() {
        super.onStart()
        permissionLauncher.launch(appPermissions.toTypedArray())
        fileChecking()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun fileChecking() {
        takeIf { File(getExternalFilesDir(null), "nanda.db").exists() }
            ?.run { databaseDownloadAlertDialog() }
            ?: run { toast("데이터베이스가 존재합니다. 그대로 진행 합니다") }
    }

    private fun databaseDownloadAlertDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("다운로드 요청")
            .setMessage("어플 사용을 위해 데이터베이스를 다운로드 합니다.")
            .setPositiveButton("확인") { _, _ ->
                toast("서버로부터 데이터베이스를 요청합니다.")
                downloadDatabase2()
            }
            .create()
            .show()
    }

    private fun downloadDatabase2() {
        ContextCompat.registerReceiver(
            this,
            receiver,
            IntentFilter().apply {
                addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            },
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )

        DownloadManager.Request(Uri.parse(applicationContext.getString(R.string.db_path)))
            .setTitle("Downloading")
            .setDescription("Downloading Database file")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(
                Uri.fromFile(File(applicationContext.getExternalFilesDir(null), "nanda.db")),
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .also(getSystemService(DownloadManager::class.java)::enqueue)
    }
}
