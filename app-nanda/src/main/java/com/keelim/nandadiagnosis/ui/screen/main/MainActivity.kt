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

import android.app.DownloadManager
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.airbnb.deeplinkdispatch.DeepLink
import com.keelim.common.extensions.toast
import com.keelim.commonAndroid.util.DownloadReceiver
import com.keelim.composeutil.ui.theme.KeelimTheme
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.screen.NandaApp
import com.keelim.shared.data.UserStateStore
import com.keelim.shared.data.model.ThemeType
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@DeepLink("all://screen/{name}")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var downloadReceiver: DownloadReceiver

    @Inject
    lateinit var userStateStore: Lazy<UserStateStore>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val themeType =
                userStateStore.get().themeTypeFlow.collectAsStateWithLifecycle(ThemeType.LIGHT).value
            val isDarkThem = when (themeType) {
                ThemeType.DARK -> true
                ThemeType.LIGHT -> false
            }
            KeelimTheme(
                isDarkTheme = isDarkThem,
            ) {
                NandaApp(
                    windowSizeClass = calculateWindowSizeClass(this),
                )
            }
            LifecycleEventEffect(
                event = Lifecycle.Event.ON_CREATE,
            ) {
                updateVisitedTime()
            }
        }
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            val parameters = intent.extras
            Timber.d("[deep link] name ${parameters?.getString("name")}")
        }
    }

    override fun onStart() {
        super.onStart()
        fileChecking()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadReceiver)
    }

    private fun fileChecking() {
        takeIf { File(getExternalFilesDir(null), "nanda.db").exists() }
            ?.run { downloadDatabase() }
            ?: run { toast("데이터베이스가 존재합니다. 그대로 진행 합니다") }
    }

    private fun downloadDatabase() {
        ContextCompat.registerReceiver(
            this,
            downloadReceiver,
            IntentFilter().apply {
                addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            },
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )

        DownloadManager.Request(applicationContext.getString(R.string.db_path).toUri())
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

    private fun updateVisitedTime() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                userStateStore.get().updateVisitedTime()
            }
        }
    }
}
