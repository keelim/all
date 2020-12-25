package com.keelim.nandadiagnosis.ui.main

import android.app.Activity
import android.app.DownloadManager
import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.keelim.nandadiagnosis.BuildConfig
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivityMainBinding
import com.keelim.nandadiagnosis.utils.BackPressCloseHandler
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var backPressCloseHandler: BackPressCloseHandler
    private lateinit var binding: ActivityMainBinding
    private val file by lazy { File(getExternalFilesDir(null), "nanda.db") }
    private val url by lazy { getString(R.string.db_path) }

    private lateinit var downloadManager: DownloadManager
    private var downloadId: Long = -1L

    companion object {
        const val updateCode = 2
    }

    private val request: DownloadManager.Request by inject { parametersOf(url, file) }

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
                        DownloadManager.STATUS_SUCCESSFUL -> Toast.makeText(
                            context,
                            "Download succeeded",
                            Toast.LENGTH_SHORT
                        ).show()

                        DownloadManager.STATUS_FAILED -> Toast.makeText(
                            context,
                            "Download failed",
                            Toast.LENGTH_SHORT
                        ).show()
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

        fileChecking() // 데이터 베이스 파일이 있는지 확인한다.

        checkingAppUpdate()

        val mAdView = AdView(this)
        mAdView.adSize = AdSize.SMART_BANNER
        mAdView.adUnitId = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/6300978111"
        else BuildConfig.API_KEY3

        binding.adview.addView(mAdView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun checkingAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    updateCode
                )
                Toast.makeText(this@MainActivity, "업데이트를 시작합니다", Toast.LENGTH_SHORT).show()
            } else
                Log.d("InAppUpdate", "Android latest Version")
        }

        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) popUpSnackbarForCompleteUpdate()
        }.apply {
            appUpdateManager.registerListener(this)
        }

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

    private fun popUpSnackbarForCompleteUpdate() {
        Snackbar.make(binding.root, "업데이트를 다운로드 하고 있습니다.", Snackbar.LENGTH_INDEFINITE).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.colorAccent, this@MainActivity.theme))
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == updateCode) {
            when (resultCode) {
                RESULT_OK -> Snackbar.make(binding.root, "업데이트를 성공적으로 완료했습니다.", Snackbar.LENGTH_LONG).show()

                Activity.RESULT_CANCELED -> Snackbar.make(binding.root, "업데이트를 취소하였습니다.", Snackbar.LENGTH_LONG).show()

                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> Snackbar.make(binding.root, "시스템 오류가 발생했습니다.", Snackbar.LENGTH_LONG).show()
            }
        }
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