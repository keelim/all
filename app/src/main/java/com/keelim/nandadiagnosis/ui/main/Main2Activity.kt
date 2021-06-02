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
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import com.keelim.common.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivityMain2Binding
import com.keelim.nandadiagnosis.service.TerminateService
import com.keelim.nandadiagnosis.utils.MaterialDialog
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.message
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.negativeButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.positiveButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.title
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class Main2Activity : AppCompatActivity() {
  private lateinit var binding: ActivityMain2Binding

  private val mainViewModel by viewModels<MainViewModel>()
  private lateinit var downloadManager: DownloadManager

  val recevier = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
      if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
        if (id == -1L) {
          val query = DownloadManager.Query().apply { setFilterById(id) }
          val cursor = downloadManager.query(query)

          if (!cursor.moveToFirst()) return
          val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

          when (cursor.getInt(columnIndex)) {
            DownloadManager.STATUS_SUCCESSFUL -> toast("다운로드가 완료되었습니다.")
            DownloadManager.STATUS_FAILED -> toast("다운로드가 실패되었습니다")
          }
        }
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMain2Binding.inflate(layoutInflater)
    setContentView(binding.root)

    initNavigation()
    initBottomAppBar()

    binding.searchButton.setOnClickListener {
      navController().navigate(R.id.navigation_search)
    }

    fileChecking()
    createNotification()
    startService(Intent(this, TerminateService::class.java))
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

  private fun initBottomAppBar() {
    binding.bottomAppBar.setNavigationOnClickListener {
      showMenu()
    }

    binding.bottomAppBar.setOnMenuItemClickListener {
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
    if (!check.exists()) databaseDownloadAlertDialog()
    else Toast.makeText(this, "데이터베이스가 존재합니다. 그대로 진행 합니다", Toast.LENGTH_SHORT).show()
  }

  private fun databaseDownloadAlertDialog() {
    MaterialDialog.createDialog(this) {
      title("다운로드 요청")
      message("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
      positiveButton(getString(R.string.ok)) {
        toast("서버로부터 데이터 베이스를 요청 합니다. ")
        downloadDatabase()
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

  private fun createNotification() {
    val intent = Intent(this, Main2Activity::class.java)
    val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val channelId = getString(R.string.my_notification_channel_id)

    val notificationBuilder = NotificationCompat.Builder(this, channelId)
      .setContentTitle("난다 진단")
      .setContentText("앱 실행 중")
      .setContentIntent(pIntent)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setSmallIcon(R.mipmap.ic_launcher)

    getSystemService(NotificationManager::class.java).run {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
          NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
        createNotificationChannel(channel)
      }
      notify(0, notificationBuilder.build())
    }
  }

  private fun navController() = findNavController(R.id.nav_host_fragment)

  private fun showMoreOptions() = navController().navigate(R.id.moreBottomSheetDialog)

  private fun showMenu() = navController().navigate(R.id.menuBottomSheetDialogFragment)
}
