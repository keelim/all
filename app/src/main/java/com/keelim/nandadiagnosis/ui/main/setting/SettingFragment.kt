package com.keelim.nandadiagnosis.ui.main.setting


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.OpenSourceActivity
import com.keelim.nandadiagnosis.ui.WebActivity
import okhttp3.Callback
import okhttp3.Response
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

            else -> false
        }
    }

    private inner class CallBackDownloadFile : Callback {

        @RequiresApi(Build.VERSION_CODES.N)
        private val fileToBeDownloaded: File = File(requireActivity().dataDir.absolutePath + "/databases", "nanda.db")


        override fun onFailure(call: okhttp3.Call, e: IOException) {
            Toast.makeText(requireActivity(), "파일을 다운로드 할 수 없습니다. 인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(call: okhttp3.Call, response: Response) {
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