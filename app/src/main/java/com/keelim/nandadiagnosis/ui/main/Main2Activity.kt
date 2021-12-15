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
package com.keelim.nandadiagnosis.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.keelim.common.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.compose.ui.CircularIndeterminateProgressBar
import com.keelim.nandadiagnosis.data.worker.DownloadWorker
import com.keelim.nandadiagnosis.databinding.ActivityMain2Binding
import com.keelim.nandadiagnosis.service.TerminateService
import com.keelim.nandadiagnosis.utils.DownloadReceiver
import com.keelim.nandadiagnosis.utils.MaterialDialog
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.message
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.negativeButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.positiveButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.title
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class Main2Activity : AppCompatActivity() {
  private lateinit var downloadManager: DownloadManager
  private val binding: ActivityMain2Binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }

  private val mainViewModel: MainViewModel by viewModels()

  private val auth by lazy { Firebase.auth }

  @Inject
  lateinit var recevier: DownloadReceiver

  private val workManager by lazy {
    WorkManager.getInstance(applicationContext)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    startService(Intent(this, TerminateService::class.java))
    initNavigation()
    initBottomAppBar()
    observeLoading()
    fileChecking()
    loginCheck()
  }

  override fun onDestroy() {
    super.onDestroy()
    stopService(Intent(this, TerminateService::class.java))
    unregisterReceiver(recevier)
  }

  private fun initNavigation() {
    navController().addOnDestinationChangedListener { _, destination, _ ->
      when (destination.id) {
        R.id.navigation_category -> {
          binding.bottomAppBar.visibility = View.VISIBLE
          binding.searchButton.show()
        }
        else -> {
          binding.bottomAppBar.visibility = View.GONE
          binding.searchButton.hide()
        }
      }
    }
  }

  private fun initBottomAppBar() = with(binding) {
    searchButton.setOnClickListener {
      mainViewModel.loadingOn()
      navController().navigate(R.id.navigation_search)
      mainViewModel.loadingOff()
    }

    bottomAppBar.setNavigationOnClickListener {
      mainViewModel.loadingOn()
      showMenu()
      mainViewModel.loadingOff()
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
    if (check.exists().not())
      databaseDownloadAlertDialog()
    else
      toast("데이터베이스가 존재합니다. 그대로 진행 합니다")
  }

  private fun databaseDownloadAlertDialog() {
    MaterialDialog.createDialog(this) {
      title("다운로드 요청")
      message("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
      positiveButton(getString(R.string.ok)) {
        toast("서버로부터 데이터 베이스를 요청 합니다. ")
        downloadDatabase2()
      }
      negativeButton(getString(R.string.cancel))
    }.show()
  }

  private fun downloadDatabase() {
    downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val intentFilter: IntentFilter = IntentFilter().apply {
      addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
      addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
    }
    registerReceiver(recevier, intentFilter)

    downloadManager.enqueue(
      DownloadManager.Request(Uri.parse(getString(R.string.db_path)))
        .setTitle("Downloading")
        .setDescription("Downloading Database file")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationUri(Uri.fromFile(File(getExternalFilesDir(null), "nanda.db")))
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
    )
  }

  private fun downloadDatabase2() {
    val constraints = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .setRequiresStorageNotLow(true)
      .setRequiresBatteryNotLow(true)
      .build()

    val downloadWorker = OneTimeWorkRequestBuilder<DownloadWorker>()
      .setConstraints(constraints)
      .addTag("downloadWork")
      .build()

    // 2
    workManager.enqueueUniqueWork(
      "oneTimeDownload",
      ExistingWorkPolicy.KEEP,
      downloadWorker
    )
    observeWork(downloadWorker.id)
  }

  private fun observeWork(id: UUID) {
    // 1
    workManager.getWorkInfoByIdLiveData(id)
      .observe(this) { info ->
        // 2
        info?.let {
          when (it.state) {
            WorkInfo.State.ENQUEUED -> toast("다운로드를 시작합니다.")
            WorkInfo.State.RUNNING -> mainViewModel.loadingOn()
            WorkInfo.State.SUCCEEDED -> mainViewModel.loadingOff()
            WorkInfo.State.FAILED -> toast("다운로드를 완료하지 못했습니다..")
            WorkInfo.State.BLOCKED -> toast("다운로드를 완료하지 못했습니다..")
            WorkInfo.State.CANCELLED -> toast("다운로드를 완료하지 못했습니다..")
          }
        }
      }
  }

  private fun loginCheck() {
    auth.currentUser ?: toast("Login 을 하시면 더 많은 서비스를 확인할 수 있습니다. ")
    if (auth.currentUser == null) {
      Toast.makeText(this, "Login 을 하시면 더 많은 서비스를 확인할 수 있습니다. ", Toast.LENGTH_SHORT).show()
    }
  }

  private fun navController() = findNavController(R.id.nav_host_fragment)

  private fun showMoreOptions() = navController().navigate(R.id.moreBottomSheetDialog)

  private fun showMenu() = navController().navigate(R.id.menuBottomSheetDialogFragment)

  private fun observeLoading() = mainViewModel.loading.observe(this) {
    binding.composeView.apply {
      bringToFront()
      setContent {
        CircularIndeterminateProgressBar(
          isDisplayed = it
        )
      }
    }
  }
}
