package com.keelim.nandadiagnosis.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.keelim.nandadiagnosis.BuildConfig
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivitySplashBinding
import com.keelim.nandadiagnosis.ui.main.MainActivity
import java.util.*

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var binding: ActivitySplashBinding


    private val runnable = Runnable {
        startActivity(Intent(this, MainActivity::class.java))
        finish() //앱을 종료한다.
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) //애니메이션을 넣어준다.
    }

    private var listener = object : PermissionListener {
        override fun onPermissionGranted() {
            Snackbar.make(binding.containerSplash, "모든 권한이 승인 되었습니다. ", Snackbar.LENGTH_SHORT).show()

            interstitialAd = InterstitialAd(this@SplashActivity)
            interstitialAd.adUnitId = getString(R.string.real_ad)
            interstitialAd.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    interstitialAd.show()
                }

                override fun onAdClosed() {}

                override fun onAdFailedToLoad(i: Int) {
                    Toast.makeText(this@SplashActivity, "ad load fail", Toast.LENGTH_SHORT).show()
                }
            } //전면광고 셋팅

            interstitialAd.loadAd(AdRequest.Builder().build())
        }

        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            Toast.makeText(this@SplashActivity, deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
            Thread.sleep(3000)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        Snackbar.make(binding.containerSplash, "NANDA 진단에 오신 것을 환영합니다.", Snackbar.LENGTH_SHORT).show()

        binding.versionName.text = BuildConfig.VERSION_NAME

        TedPermission.with(this)
                .setPermissionListener(listener)
                .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
    }

    override fun onBackPressed() {}
}


