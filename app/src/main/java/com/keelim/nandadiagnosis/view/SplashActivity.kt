package com.keelim.nandadiagnosis.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.snackbar.Snackbar
import com.keelim.nandadiagnosis.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {
    private lateinit var interstitialAd: InterstitialAd

    private val runnable = Runnable {
        startActivity(Intent(this, MainActivity::class.java))
        finish() //앱을 종료한다.
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) //애니메이션을 넣어준다.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Snackbar.make(container_splash, "NANDA 진단에 오신 것을 환영합니다.", Snackbar.LENGTH_SHORT).show()

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.real_ad)
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                interstitialAd.show()
            }

            override fun onAdClosed() {
                Handler().postDelayed(runnable, 500) //handler를 통하여 사용
            }

            override fun onAdFailedToLoad(i: Int) {
                Log.e("Error", "ad loading fail")
                Handler().postDelayed(runnable, 500) //handler를 통하여 사용
            }
        } //전면광고 셋팅
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }
}


