package com.keelim.mygrade.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.keelim.mygrade.BuildConfig
import com.keelim.data.model.Result
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.ActivityGradeBinding
import com.keelim.mygrade.ui.center.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class GradeActivity : AppCompatActivity() {
    private val data: Result? by lazy { intent.getParcelableExtra("data") }
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityGradeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityGradeBinding>(
            this,
            R.layout.activity_grade
        ).apply {
            grade.text = data?.grade.orEmpty()
            level.text = data?.point.orEmpty()
            btnCopy.setOnClickListener {
                saveAndCopy()
            }
        }.also {
            binding = it
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun saveAndCopy() {
        val screenBitmap = window.decorView.rootView.drawToBitmap()
        runCatching {
            val cachePath = File(applicationContext.cacheDir, "images").apply {
                mkdirs()
            }
            val stream = FileOutputStream("$cachePath/image.png")
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
            FileProvider.getUriForFile(
                applicationContext,
                "com.keelim.fileprovider", File(cachePath, "image.png")
            )
        }.onSuccess {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, it)
            }, "Share Capture Image"))
        }.onFailure {
            it.printStackTrace()
        }
    }
}
