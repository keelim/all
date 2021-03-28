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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.keelim.nandadiagnosis.BuildConfig
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivityMainBinding
import com.keelim.nandadiagnosis.utils.BackPressCloseHandler
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.io.File

class MainActivity : AppCompatActivity() {
  private lateinit var backPressCloseHandler: BackPressCloseHandler
  private lateinit var binding: ActivityMainBinding

  private lateinit var downloadManager: DownloadManager
  private var downloadId: Long = -1L

  private val file by lazy { File(getExternalFilesDir(null), "nanda.db") }
  private val url by lazy { getString(R.string.db_path) }
  private val request: DownloadManager.Request by inject { parametersOf(url, file) }

  private val onDownloadComplete = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
      if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
        if (downloadId == id) {
          val query = DownloadManager.Query().apply { setFilterById(id) }
          val cursor = downloadManager.query(query)

          if (!cursor.moveToFirst()) return
          val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

          when (cursor.getInt(columnIndex)) {
            DownloadManager.STATUS_SUCCESSFUL -> Toast.makeText(context, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show()

            DownloadManager.STATUS_FAILED -> Toast.makeText(context, "다운로드가 실패되었습니다", Toast.LENGTH_SHORT).show()
          }
        }
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    backPressCloseHandler = BackPressCloseHandler(this)

    val navController = findNavController(R.id.nav_host_fragment)
    binding.navView.setupWithNavController(navController)

    supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

    val radius = resources.getDimension(R.dimen.radius_small)
    val bottomNavigationViewBackground = binding.navView.background as MaterialShapeDrawable
    bottomNavigationViewBackground.shapeAppearanceModel =
      bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
        .setTopRightCorner(CornerFamily.ROUNDED, radius)
        .setTopLeftCorner(CornerFamily.ROUNDED, radius)
        .build()

    fileChecking()

    val mAdView = AdView(this)
    mAdView.adSize = AdSize.SMART_BANNER
    mAdView.adUnitId = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/6300978111"
    else "ca-app-pub-3115620439518585/8213873659"

    binding.adview.addView(mAdView)
    val adRequest = AdRequest.Builder().build()
    mAdView.loadAd(adRequest)
  }

  private fun fileChecking() {
    val check = File(getExternalFilesDir(null), "nanda.db")
    if (!check.exists()) databaseDownloadAlertDialog()
    else Toast.makeText(this, "데이터베이스가 존재합니다. 그대로 진행 합니다", Toast.LENGTH_SHORT).show()
  }

  private fun databaseDownloadAlertDialog() {
    AlertDialog.Builder(this)
      .setTitle("다운로드 요청")
      .setMessage("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
      .setCancelable(false)
      .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
        Toast.makeText(this, "서버로부터 데이터 베이스를 요청 합니다. ", Toast.LENGTH_SHORT).show()
        downloadDatabase()
      }.create()
      .show()
  }

  private fun downloadDatabase() {
    downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    IntentFilter().apply {
      addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
      addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
      registerReceiver(onDownloadComplete, this)
    }

    downloadId = downloadManager.enqueue(request)
  }

  override fun onBackPressed() {
    backPressCloseHandler.onBackPressed()
  }
}
