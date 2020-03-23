package com.keelim.cnubus.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity(R.layout.activity_splash) {
    private lateinit var interstitialAd: InterstitialAd
    private val runnable = Runnable {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) //애니메이션을 넣어준다.
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Snackbar.make(splash_container, "충남대버스에 오신것을 환영 합니다.", Snackbar.LENGTH_SHORT).show()

        AppCenter.start(
            application, "e2bf6b60-7432-41b7-bde0-e88d697ca4a4",
            Analytics::class.java, Crashes::class.java
        )

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
                Log.e("Error", "광고 로딩 실패")
                Handler().postDelayed(runnable, 500) //handler를 통하여 사용
            }
        } //전면광고 셋팅

        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }

}