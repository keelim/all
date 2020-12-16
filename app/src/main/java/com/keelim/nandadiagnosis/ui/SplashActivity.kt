package com.keelim.nandadiagnosis.ui

import android.Manifest
import android.content.Intent
import android.os.Build
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
import com.keelim.nandadiagnosis.BuildConfig
import com.keelim.nandadiagnosis.databinding.ActivitySplashBinding
import com.keelim.nandadiagnosis.ui.main.MainActivity
import java.util.*

class SplashActivity : AppCompatActivity() {
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var binding: ActivitySplashBinding
    private val test = "ca -app-pub-3940256099942544/1033173712"
    private infix fun String.or(that: String): String = if (BuildConfig.DEBUG) this else that

    private var listener = object : PermissionListener {
        override fun onPermissionGranted() {
            Snackbar.make(binding.root, "모든 권한이 승인 되었습니다. ", Snackbar.LENGTH_SHORT).show()

            interstitialAd = InterstitialAd(this@SplashActivity)
//            interstitialAd.adUnitId = getString(R.string.real_ad)
            interstitialAd.adUnitId = test or BuildConfig.API_KEY
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
        Snackbar.make(binding.root, "NANDA 진단에 오신 것을 환영합니다.", Snackbar.LENGTH_SHORT).show()

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


    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        arrayOf(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE)
    } else {
        arrayOf(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    /*private fun observePermission(){
        permissions.toObservable()
                .filter {  ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED}
                .toList()
                .map{permissions.sortedArray()}
                .subscribe {
                    permissionList:Array<String>?, _:Throwable ->
                    permissionList?.let {
                        ActivityCompat.requestPermissions(this, it, 0)
                    }
                }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Observables.zip(
                permissions.toObservable(),
                grantResults.toObservable())
                .filter {
                    it.second != PackageManager.PERMISSION_GRANTED
                }.count()
                .subscribe { permissionCount: Long?, t2: Throwable? ->
                    if (permissionCount == 0L) {//zero means all permissions granted
                        Handler(Looper.getMainLooper()).postDelayed({
                               Intent(this@SplashActivity, MainActivity::class.java).apply {
                                   startActivity(this)
                                   finish()
                               }
                        }, 3000)
                    } else {
                        Toast.makeText(this@SplashActivity, "This application is not granted please reinstall me", Toast.LENGTH_SHORT).show()
                        finishAffinity()
                    }
                }
    }*/
}


