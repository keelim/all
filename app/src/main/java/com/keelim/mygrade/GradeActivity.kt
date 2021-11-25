package com.keelim.mygrade

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
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
        grade.text = data?.grade.orEmpty()
        level.text = data?.point.orEmpty()

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent())
        finish()
    }
}