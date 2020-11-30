package com.keelim.nandadiagnosis.ui.main

import android.app.Activity
import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivityMainBinding
import com.keelim.nandadiagnosis.util.BackPressCloseHandler


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var backPressCloseHandler: BackPressCloseHandler
    private lateinit var binding: ActivityMainBinding

    private lateinit var downloadManager: DownloadManager
    private var downloadId: Long = -1L

    companion object {
        const val updateCode = 2
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
                if (downloadId == id) {
                    val query = DownloadManager.Query().apply {
                        setFilterById(id)
                    }
                    val cursor = downloadManager.query(query)

                    if (!cursor.moveToFirst()) return
                    val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

                    when (cursor.getInt(columnIndex)) {
                        DownloadManager.STATUS_SUCCESSFUL -> Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()
                        DownloadManager.STATUS_FAILED -> Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        backPressCloseHandler = BackPressCloseHandler(this)

        val appBarConfiguration = AppBarConfiguration.Builder(R.id.navigation_category, R.id.navigation_search, R.id.navigation_setting)
                .build()

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)

        fileChecking() // 데이터 베이스 파일이 있는지 확인한다.
        checkingAppUpdate()
    }

    private fun checkingAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, updateCode)
                Toast.makeText(this@MainActivity, "업데이트를 시작합니다", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this@MainActivity, "최신 버전 어플리케이션 사용해주셔서 감사합니다", Toast.LENGTH_SHORT).show()
        }

        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED)
                popUpSnackbarForCompleteUpdate()
        }.apply {
            appUpdateManager.registerListener(this)
        }

    }

    private fun fileChecking() {
        val check = getDatabasePath("nanda.db")

        if (!check.exists()) databaseDownloadAlertDialog()
        else Toast.makeText(this, "데이터베이스가 존재합니다. 그대로 진행 합니다", Toast.LENGTH_SHORT).show()
    }


    private fun databaseDownloadAlertDialog() {
        binding.mainProgressbar.visibility = View.VISIBLE

        AlertDialog.Builder(this)
                .setTitle("다운로드 요청")
                .setMessage("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                    Toast.makeText(this, "서버로부터 데이터 베이스를 요청 합니다. ", Toast.LENGTH_SHORT).show()
                    downloadDatabase()
                }.create()
                .show()

        binding.mainProgressbar.visibility = View.INVISIBLE

    }

    private fun popUpSnackbarForCompleteUpdate() {
        Snackbar.make(binding.container, "업데이트를 다운로드 하고 있습니다.", Snackbar.LENGTH_INDEFINITE).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.colorAccent, this@MainActivity.theme))
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == updateCode) {
            when (resultCode) {
                RESULT_OK -> Snackbar.make(binding.container, "업데이트를 성공적으로 완료했습니다.", Snackbar.LENGTH_LONG).show()
                Activity.RESULT_CANCELED -> Snackbar.make(binding.container, "업데이트를 취소하였습니다.", Snackbar.LENGTH_LONG).show()
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> Snackbar.make(binding.container, "시스템 오류가 발생했습니다.", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    private fun downloadDatabase() {
        val file = getDatabasePath("nanda.db")
        val url = getString(R.string.db_path)

        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            registerReceiver(onDownloadComplete, this)
        }

        val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("Downloading")
                .setDescription("Downloading Database file")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadId = downloadManager.enqueue(request)
    }

    override fun onDestroy() {
        unregisterReceiver(onDownloadComplete)
        super.onDestroy()
    }

    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }
}