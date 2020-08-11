package com.keelim.nandadiagnosis.view.fragments

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.Call
import android.util.Log

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.view.OpenSourceActivity
import com.keelim.nandadiagnosis.view.WebActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            "blog" -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(("https://blog.naver.com/cjhdori"))))
                return true
            }

            "nandaHome" -> {
                Intent(context, WebActivity::class.java).apply {
                    Toast.makeText(activity, "홈페이지 재구성 중 입니다.", Toast.LENGTH_SHORT).show()
                }
                true
            }
            "opensource" -> {
                startActivity(Intent(context, OpenSourceActivity::class.java))
                return true
            }

            "version" -> {
                preference.summary = BuildConfig.VERSION_NAME
                return true
            }

            else -> false
        }
    }

    private fun download() {
        AlertDialog.Builder(this)
                .setTitle("다운로드 요청")
                .setMessage("재 다운로드 합니다. ")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                    Toast.makeText(requireActivity(), "서버로부터 데이터 베이스를 요청 합니다. ", Toast.LENGTH_SHORT).show()
                    val request = Request.Builder()
                            .url(getString(R.string.db_path))
                            .build()

                    OkHttpClient().newCall(request).enqueue(CallBackDownloadFile())
                }.create()
                .show()

    }

    private inner class CallBackDownloadFile internal constructor() : Callback {

        private val fileToBeDownloaded: File = File(requireActivity().dataDir.absolutePath + "/databases", "nanda.db")

        override fun onFailure(call: Call, e: IOException) {
            Toast.makeText(requireActivity(), "파일을 다운로드 할 수 없습니다. 인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show()
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            try {
                val flag = fileToBeDownloaded.createNewFile()
            } catch (e: IOException) {
                Log.e("Error", e.message!!)
                Toast.makeText(requireActivity(), "다운로드 파일을 생성할 수 없습니다.\n 데이터베이스 부족으로 인해 종료 합니다. ", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireActivity(), "다운로드가 완료되었습니다. ", Toast.LENGTH_SHORT).show()
        }
    }


}