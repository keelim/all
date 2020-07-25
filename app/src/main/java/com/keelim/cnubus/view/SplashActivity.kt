package com.keelim.cnubus.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.keelim.cnubus.R
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.ArrayList


class SplashActivity : AppCompatActivity(R.layout.activity_splash) {
    private lateinit var interstitialAd: InterstitialAd
    private val runnable = Runnable {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) //애니메이션을 넣어준다.
        finish()
    }

    private var listener = object : PermissionListener {
        override fun onPermissionGranted() {
            Snackbar.make(splash_container, "모든 권한이 승인 되었습니다. ", Snackbar.LENGTH_SHORT).show()
        }

        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            Toast.makeText(this@SplashActivity, deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Snackbar.make(splash_container, "충남대버스에 오신것을 환영 합니다.", Snackbar.LENGTH_SHORT).show()

        AppCenter.start(
            application, getString(R.string.appcenter),
            Analytics::class.java, Crashes::class.java
        )

        TedPermission.with(this)
            .setPermissionListener(listener)
            .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(Manifest.permission.INTERNET,Manifest.permission.WAKE_LOCK, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.VIBRATE)
            .check()

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.real_ad)
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                interstitialAd.show()
            }

            override fun onAdClosed() {
                Handler(Looper.getMainLooper()).postDelayed(runnable, 500) //handler를 통하여 사용
                finish()
            }

            override fun onAdFailedToLoad(i: Int) {
                Log.e("Error", "광고 로딩 실패")
                Handler(Looper.getMainLooper()).postDelayed(runnable, 500) //handler를 통하여 사용
                finish()
            }
        } //전면광고 셋팅

        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }

    override fun onBackPressed() {

    }

}