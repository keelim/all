package com.keelim.nandadiagnosis.ui.main

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.keelim.nandadiagnosis.utils.BackPressCloseHandler
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPressCloseHandler = BackPressCloseHandler(this)
        val appBarConfiguration = AppBarConfiguration.Builder(R.id.navigation_category, R.id.navigation_search, R.id.navigation_setting)
                .build()

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(nav_view, navController)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileChecking()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(this, "버전이 맞지 않아 종료 합니다", Toast.LENGTH_SHORT).show()
                finish()
            }, 3000)
        }


        // appUpdate
        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                            AppUpdateType.FLEXIBLE)) {
                appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                        AppUpdateType.FLEXIBLE,
                        // The current activity making the update request.
                        this,
                        // Include a request code to later monitor this update request.
                        2
                )
                Snackbar.make(container, "업데이트를 시작합니다.", Snackbar.LENGTH_SHORT).show()
            } else
                Snackbar.make(container, "최신 버전 어플리케이션 사용해주셔서 감사합니다.", Snackbar.LENGTH_SHORT).show()
        }

        val listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED)
                popUpSnackbarForCompleteUpdate()
        }
        appUpdateManager.registerListener(listener)

    }


    private fun fileChecking() {
        val check = File(dataDir.absolutePath + "/databases/nanda.db")
        if (!check.exists()) { //데이터베이스를 받아온다.
            alertBuilderSetting()

        } else Toast.makeText(this, "데이터베이스가 존재합니다. 그대로 진행 합니다.", Toast.LENGTH_SHORT).show()
    }


    private fun alertBuilderSetting() { //okhttp 작동 방식은 나중에 확인을 해보자
        main_progressbar.visibility = View.VISIBLE

        AlertDialog.Builder(this)
                .setTitle("다운로드 요청")
                .setMessage("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                    Toast.makeText(this, "서버로부터 데이터 베이스를 요청 합니다. ", Toast.LENGTH_SHORT).show()
                    val request = Request.Builder()
                            .url(getString(R.string.db_path))
                            .build()

                    OkHttpClient().newCall(request).enqueue(CallBackDownloadFile())
                }.create()
                .show()

        main_progressbar.visibility = View.INVISIBLE

    }


    private fun popUpSnackbarForCompleteUpdate() {
        Snackbar.make(container, "업데이트를 다운로드 하고 있습니다.", Snackbar.LENGTH_INDEFINITE).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.colorAccent, this@MainActivity.theme))
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            when (resultCode) {
                RESULT_OK -> Snackbar.make(container, "업데이트를 성공적으로 완료했습니다.", Snackbar.LENGTH_LONG).show()

                Activity.RESULT_CANCELED -> Snackbar.make(container, "업데이트를 취소하였습니다.", Snackbar.LENGTH_LONG).show()

                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> Snackbar.make(container, "시스템 오류가 발생했습니다.", Snackbar.LENGTH_LONG).show()

            }

        }
    }

    private inner class CallBackDownloadFile : Callback {

        private val fileToBeDownloaded: File = File(dataDir.absolutePath + "/databases", "nanda.db")

        override fun onFailure(call: okhttp3.Call, e: IOException) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "파일을 다운로드 할 수 없습니다. 인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        override fun onResponse(call: okhttp3.Call, response: Response) {
            try {
                val flag = fileToBeDownloaded.createNewFile()
                Log.e("file create", "파일 만들기: $flag")
            } catch (e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "다운로드 파일을 생성할 수 없습니다.\n 데이터베이스 부족으로 인해 종료 합니다. ", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            val inputStream = response.body!!.byteStream()
            val outputStream = FileOutputStream(fileToBeDownloaded)
            val buffer = 2046
            val data = ByteArray(buffer)
            var count: Int
            while (inputStream.read(data).also { count = it } != -1) {
                outputStream.write(data, 0, count)
            }

            outputStream.run {
                flush()
                close()
            }

            inputStream.close()


            Toast.makeText(this@MainActivity, "다운로드가 완료되었습니다. ", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }
}