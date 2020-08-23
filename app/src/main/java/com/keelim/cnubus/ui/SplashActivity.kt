package com.keelim.cnubus.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val runnable = Runnable {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) //애니메이션을 넣어준다.
            finish()
        }
    }

    private var listener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(this@SplashActivity, "모든 권한이 승인 되었습니다. ", Toast.LENGTH_SHORT).show()
            
            Handler(Looper.getMainLooper()).postDelayed(runnable, 3000) //권한이 전부 승인되면 실행
        }

        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            Toast.makeText(this@SplashActivity, deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Snackbar.make(splash_container, "충남대버스에 오신것을 환영 합니다.", Snackbar.LENGTH_SHORT).show()

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