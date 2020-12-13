package com.keelim.nandadiagnosis.ui.main.setting


import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.OpenSourceActivity
import com.keelim.nandadiagnosis.ui.WebActivity
import java.io.File

class SettingFragment : PreferenceFragmentCompat() {
    private lateinit var downloadManager: DownloadManager
    private var downloadId: Long = -1L

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


    override fun onAttach(context: Context) {
        super.onAttach(context)

        downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val intentFilter = IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            requireActivity().registerReceiver(onDownloadComplete, this)
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            "blog" -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse((getString(R.string.blog_url)))))
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

            "db_download" -> {
                Toast.makeText(activity, "다운로드 동안 잠시만 기다려 주세요", Toast.LENGTH_SHORT).show()
                downloadDatabase()
                return true
            }

            "review" -> {
                val manager = ReviewManagerFactory.create(requireActivity())

                manager.requestReviewFlow().apply {
                    addOnCompleteListener {
                        if (this.isSuccessful) {
                            manager.launchReviewFlow(requireActivity(), this.result)
                        }
                    }
                }
                return true
            }

            else -> false
        }
    }

    private fun downloadDatabase() { //데이터베이스를 다운로드 받는다
        val file = File(requireActivity().getExternalFilesDir(null), "nanda.db")
        val url = requireActivity().getString(R.string.db_path)

        val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("Downloading")
                .setDescription("Downloading Database file")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadId = downloadManager.enqueue(request)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().unregisterReceiver(onDownloadComplete)
    }

}