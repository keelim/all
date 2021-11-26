package com.keelim.mygrade

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.keelim.mygrade.databinding.ActivityGradeBinding
import dagger.hilt.android.AndroidEntryPoint


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


    private fun initViews()  = with(binding){
        val result  =data?.grade.orEmpty() + data?.point.orEmpty()
        grade.text = data?.grade.orEmpty()
        level.text = data?.point.orEmpty()
        val ad = AdView(this@GradeActivity).apply {
            adSize = AdSize.BANNER
            adUnitId = if (BuildConfig.DEBUG.not()) {
                BuildConfig.key
            } else {
                "ca-app-pub-3940256099942544/6300978111"
            }
        }
        adView.addView(ad)
        val adRequest = AdRequest.Builder().build()
        ad.loadAd(adRequest)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent())
        finish()
    }
}