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
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keelim.common.extensions.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivityMain2Binding
import com.keelim.nandadiagnosis.di.DownloadReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class Main2Activity : AppCompatActivity() {
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var receiver: DownloadReceiver

    private val appPermissions: List<String> = buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
            add(Manifest.permission.READ_MEDIA_IMAGES)
            add(Manifest.permission.READ_MEDIA_VIDEO)
            add(Manifest.permission.READ_MEDIA_AUDIO)
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
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        permissionLauncher.launch(appPermissions.toTypedArray())
        initViews()
        fileChecking()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun initViews() = with(binding) {
        navController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_category -> {
                    bottomAppBar.visibility = View.VISIBLE
                    searchButton.show()
                }
                else -> {
                    bottomAppBar.visibility = View.GONE
                    searchButton.hide()
                }
            }
        }
        searchButton.setOnClickListener {
            navController().navigate(R.id.navigation_search)
        }

        bottomAppBar.setNavigationOnClickListener {
            showMenu()
        }

        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.more -> {
                    showMoreOptions()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun fileChecking() {
        val check = File(getExternalFilesDir(null), "nanda.db")
        if (check.exists().not()) {
            databaseDownloadAlertDialog()
        } else {
            toast("데이터베이스가 존재합니다. 그대로 진행 합니다")
        }
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
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        registerReceiver(
            receiver,
            IntentFilter().apply {
                addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            },
        )

        val request = DownloadManager.Request(Uri.parse(applicationContext.getString(R.string.db_path)))
            .setTitle("Downloading")
            .setDescription("Downloading Database file")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(File(applicationContext.getExternalFilesDir(null), "nanda.db")))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        downloadManager.enqueue(request)
    }

    private fun navController(): NavController {
        return binding.navHostFragment.getFragment<NavHostFragment>().navController
    }

    private fun showMoreOptions() = navController().navigate(R.id.moreBottomSheetDialog)

    private fun showMenu() = navController().navigate(R.id.menuBottomSheetDialogFragment)
}
