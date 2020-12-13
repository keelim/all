package com.keelim.cnubus.ui

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
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.databinding.ActivitySplashBinding
import com.keelim.cnubus.ui.main.MainActivity
import java.util.*


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var interstitialAd: InterstitialAd
    private val test = "ca -app-pub-3940256099942544/1033173712"
    private infix fun String.or(that: String): String = if (BuildConfig.DEBUG) this else that
    private var listener = object : PermissionListener {
        override fun onPermissionGranted() {
            Snackbar.make(binding.root, "모든 권한이 승인 되었습니다. ", Snackbar.LENGTH_SHORT).show()

            interstitialAd = InterstitialAd(this@SplashActivity)
            interstitialAd.adUnitId = test or BuildConfig.API_KEY2
            interstitialAd.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    interstitialAd.show()
                }

                override fun onAdClosed() {}

                override fun onAdFailedToLoad(errorCode: Int) {
                    Toast.makeText(this@SplashActivity, "ad load fail", Toast.LENGTH_SHORT).show()
                    Log.e("ADMOB", errorCode.toString())
                }
            } //전면광고 셋팅
            interstitialAd.loadAd(AdRequest.Builder().build())

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish() //앱을 종료한다.
            }, 300)
        }

        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(this@SplashActivity, deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
                finish()
            }, 3000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TedPermission.with(this)
            .setPermissionListener(listener)
            .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ).check()

    }

    override fun onBackPressed() {}
}