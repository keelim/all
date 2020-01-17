package com.keelim.nandadiagnosis.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.diagnosis.DiagnosisActivity
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build()
        val nav_view = findViewById<BottomNavigationView>(R.id.nav_view)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(nav_view, navController)
        fileChecking() //데이터베이스 파일 유무 확인
    }

    private fun fileChecking() {
        val check = File(dataDir.absolutePath + "/databases/nanda.db")
        if (!check.exists()) { //데이터베이스를 받아온다.
            alertBuilderSetting()
        } else {
            Toast.makeText(this, "데이터베이스가 존재합니다. 그대로 진행 합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun click(view: View) { //xml에 연결되어 있는 페이지
        when (view.id) {
            R.id.search_view_1 -> showDialog("1")
            R.id.search_view_2 -> showDialog("2")
            R.id.search_view_3 -> showDialog("3")
            R.id.search_view_4 -> showDialog("4")
            R.id.search_view_5 -> showDialog("5")
            R.id.search_view_6 -> showDialog("6")
            R.id.search_view_7 -> showDialog("7")
            R.id.search_view_8 -> showDialog("8")
            R.id.search_view_9 -> showDialog("9")
            R.id.search_view_10 -> showDialog("10")
            R.id.search_view_11 -> showDialog("11")
            R.id.search_view_12 -> showDialog("12")
            R.id.search_view_13 -> showDialog("13")
        }
    }

    private fun alertBuilderSetting() { //okhttp 작동 방식은 나중에 확인을 해보자
        val builder = AlertDialog.Builder(this)
        builder.setTitle("다운로드 요청")
                .setMessage("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface?, i: Int ->
                    Toast.makeText(this, "서버로부터 데이터 베이스를 요청 합니다. ", Toast.LENGTH_SHORT).show()
                    val client = OkHttpClient()
                    val request = Request.Builder()
                            .url("https://github.com/keelim/Keelim.github.io/raw/master/assets/nanda.db")
                            .build()
                    val callBackDownloadFile = CallBackDownloadFile()
                    client.newCall(request).enqueue(callBackDownloadFile)
                }.create()
                .show()
    }

    private fun showDialog(num: String) { //데이터를 사용하는 페이지 이니 조심하라는 문구
        val builder = AlertDialog.Builder(this)
        builder.setMessage("이 기능은 데이터를 사용할 수 있습니다. 사용하시겠습니까?")
                .setPositiveButton("예") { _, _ -> intentList(num) }
                .setNegativeButton("아니오") { _, _ -> Toast.makeText(this, "아니오 선택했습니다.", Toast.LENGTH_LONG).show() }
                .create()
                .show()
    }

    private fun intentList(num: String) {
        val intent = Intent(this, DiagnosisActivity::class.java)
        intent.putExtra("extra", num)
        startActivity(intent)
    }

    private inner class CallBackDownloadFile internal constructor() : Callback {
        //okhttp call back method
        private val fileToBeDownloaded: File

        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "파일을 다운로드 할 수 없습니다. 인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            try {
                val flag = fileToBeDownloaded.createNewFile()
                Log.e("file create", "파일 만들기: $flag")
            } catch (e: IOException) {
                Log.e("Error", e.message)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "다운로드 파일을 생성할 수 없습니다.\n 데이터베이스 부족으로 인해 종료 합니다. ", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            val inputStream = response.body!!.byteStream()
            val outputStream: FileOutputStream = FileOutputStream(fileToBeDownloaded)
            val BUFFER_SIZE = 2046
            val data = ByteArray(BUFFER_SIZE)
            var count: Int
            while (inputStream.read(data).also { count = it } != -1) outputStream.write(data, 0, count)
            outputStream.flush()
            outputStream.close()
            inputStream.close()
            runOnUiThread { Toast.makeText(this@MainActivity, "다운로드가 완료되었습니다. ", Toast.LENGTH_SHORT).show() }
        }

        init {
            fileToBeDownloaded = File(dataDir.absolutePath + "/databases", "nanda.db")
        }
    }
}