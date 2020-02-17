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
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    //인트로 액티비티를 생성한다.
    private var handler: Handler = Handler()
    private lateinit var interstitialAd: InterstitialAd

    private val runnable = Runnable {
        //runable 작동을 하고 시작
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent) //인텐트를 넣어준다. intro -> main
        finish() //앱을 종료한다.
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) //애니메이션을 넣어준다.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Snackbar.make(splash_container, "충남대버스에 오신것을 환영 합니다.", Snackbar.LENGTH_SHORT).show()

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.real_ad)
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                interstitialAd.show()
            }

            override fun onAdClosed() {
                handler.postDelayed(runnable, 500) //handler를 통하여 사용
            }

            override fun onAdFailedToLoad(i: Int) {
                Log.e("Error", "ad loading fail")
                handler.postDelayed(runnable, 500) //handler를 통하여 사용
            }
        } //전면광고 셋팅
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }

    override fun onBackPressed() { //back 키 눌렀을 때
        super.onBackPressed()
        handler.removeCallbacks(runnable)
    }
}