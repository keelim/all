package com.keelim.nandadiagnosis.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.keelim.nandadiagnosis.R
import io.fabric.sdk.android.Fabric

class SplashActivity : AppCompatActivity() {
    //handler를 다르게 설정을 할 수 있는가?
    private var handler: Handler = Handler()
    private lateinit var interstitialAd: InterstitialAd
    // 인앱 업데이트를 등록을 하는 방법
    private val runnable = Runnable {
        //runable 작동을 하고 시작
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent) //인텐트를 넣어준다. intro -> main
        finish() //앱을 종료한다.
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) //애니메이션을 넣어준다.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics()) //Fabric 설정
        setContentView(R.layout.activity_splash)
        Toast.makeText(this, "NANDA 진단에 오신 것을 환영 합니다. ", Toast.LENGTH_SHORT).show()

        interstitialAd = InterstitialAd(this) //전면광고 셋팅
        interstitialAd.adUnitId = getString(R.string.test_ad)
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                interstitialAd.show()
            }

            override fun onAdClosed() {
                handler.postDelayed(runnable, 500) //handler를 통하여 사용
            }

            override fun onAdFailedToLoad(i: Int) {
                Log.e("Error", "ad loading fail")
            }
        }
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }

    override fun onBackPressed() { //back 키 눌렀을 때
        super.onBackPressed()
        handler.removeCallbacks(runnable)
    }
}