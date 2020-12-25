package com.keelim.nandadiagnosis.ui

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
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


class SplashActivity : AppCompatActivity() {
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var binding: ActivitySplashBinding
    private val test = "ca-app-pub-3940256099942544/1033173712"
    private lateinit var settings: SharedPreferences

    private var listener = object : PermissionListener {
        override fun onPermissionGranted() {
            Snackbar.make(binding.root, "모든 권한이 승인 되었습니다. ", Snackbar.LENGTH_SHORT).show()

            interstitialAd = InterstitialAd(this@SplashActivity)
            interstitialAd.adUnitId =
                    if(BuildConfig.DEBUG) test else BuildConfig.API_KEY
            interstitialAd.adListener = object : AdListener() {
                override fun onAdLoaded() { interstitialAd.show() }

                override fun onAdClosed() {}

                override fun onAdFailedToLoad(errorCode: Int) {
                    Toast.makeText(this@SplashActivity, "ad load fail $errorCode", Toast.LENGTH_SHORT).show()
                    Log.e("Error code", "admob $errorCode")
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
        addShortcut()

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

    private fun addShortcut() {
        settings = getSharedPreferences(PREF_FIRST_START, 0)

        if (settings.getBoolean("AppFirstLaunch", true)) {  // 아이콘이 두번 추가 안되도록 하기 위해서 필요한 체크입니다.
            settings.edit().putBoolean("AppFirstLaunch", false).apply()
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {

                val shortcutInfo = ShortcutInfoCompat.Builder(this, "#1")
                        .setIntent(Intent(this, SplashActivity::class.java).setAction(Intent.ACTION_MAIN)) // !!! intent's action must be set on oreo
                        .setShortLabel(getString(R.string.app_name)) //  아이콘에 같이 보여질 이름
                        .setIcon(IconCompat.createWithResource(this, R.mipmap.ic_launcher)) //아이콘에 보여질 이미지
                        .build()
                ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)

                Toast.makeText(this, "홈 화면에 바로가기를 추가하였습니다. ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {}

    companion object {
        const val PREF_FIRST_START = "AppFirstLaunch"
    }
}


