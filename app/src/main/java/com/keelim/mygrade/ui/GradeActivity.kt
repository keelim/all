package com.keelim.mygrade.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.keelim.mygrade.BuildConfig
import com.keelim.data.model.Result
import com.keelim.mygrade.databinding.ActivityGradeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class GradeActivity : AppCompatActivity() {
    private val data: Result? by lazy { intent.getParcelableExtra("data") }
    private val binding: ActivityGradeBinding by lazy {
        ActivityGradeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        grade.text = data?.grade.orEmpty()
        level.text = data?.point.orEmpty()
        val ad = AdView(this@GradeActivity).apply {
            adSize = AdSize.BANNER
            adUnitId = if (BuildConfig.DEBUG.not()) {
                BuildConfig.UNIT
            } else {
                "ca-app-pub-3940256099942544/6300978111"
            }
        }
        adView.addView(ad)
        val adRequest = AdRequest.Builder().build()
        ad.loadAd(adRequest)
        btnCopy.setOnClickListener {
            saveAndCopy()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent())
        finish()
    }

    private fun saveAndCopy() {
        val view = window.decorView.rootView
        val screenBitmap = getBitmapFromView(view)

        runCatching {
            val cachePath = File(applicationContext.cacheDir, "images").apply {
                mkdirs()
            }
            val stream = FileOutputStream("$cachePath/image.png")
            screenBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
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

    private fun getBitmapFromView(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bitmap))
        return bitmap
    }
}
