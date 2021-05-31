/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.nandadiagnosis.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.keelim.nandadiagnosis.BuildConfig
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.base.BaseActivity
import com.keelim.nandadiagnosis.databinding.ActivitySplashBinding
import com.keelim.nandadiagnosis.ui.main.Main2Activity
import com.keelim.nandadiagnosis.utils.MaterialDialog
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.message
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.negativeButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.positiveButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.title
import kotlinx.coroutines.delay
import timber.log.Timber

class SplashActivity : BaseActivity() {
  private var mInterstitialAd: InterstitialAd? = null
  private val binding: ActivitySplashBinding by binding(R.layout.activity_splash)
  private val test = "ca-app-pub-3940256099942544/1033173712"
  private infix fun String.or(that: String): String = if (BuildConfig.DEBUG) this else that

  private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    arrayOf(
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.INSTALL_SHORTCUT,
      Manifest.permission.INTERNET,
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.FOREGROUND_SERVICE
    )
  } else {
    arrayOf(
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.INSTALL_SHORTCUT,
      Manifest.permission.INTERNET,
      Manifest.permission.READ_EXTERNAL_STORAGE
    )
  }

  companion object {
    const val MULTIPLE_PERMISSIONS = 8888
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (hasPermissions(permissions)) { // 권한이 있는 경우
      goNext()
    } else {
      ActivityCompat.requestPermissions(this, permissions, MULTIPLE_PERMISSIONS)
    }
  }

  private fun showAd() {
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(
      this, test or "ca-app-pub-3115620439518585/4013096159", adRequest,
      object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
          Timber.d(adError.message)
          mInterstitialAd = null
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
          Timber.d("Ad was loaded.")
          mInterstitialAd = interstitialAd

          if (mInterstitialAd != null) {
            mInterstitialAd!!.show(this@SplashActivity)
          } else {
            Timber.d("The interstitial ad wasn't ready yet.")
          }
        }
      }
    )
  }

  private fun hasPermissions(permissions: Array<String>): Boolean {
    permissions.forEach { permission ->
      if (ActivityCompat.checkSelfPermission(
          this,
          permission
        ) != PackageManager.PERMISSION_GRANTED
      )
        return false
    }
    return true
  }

  // 권한 요청에 대한 결과 처리
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    when (requestCode) {
      MULTIPLE_PERMISSIONS -> {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          Snackbar.make(binding.root, "모든 권한이 승인 되었습니다. ", Snackbar.LENGTH_SHORT).show()

          showAd()
          goNext()
        } else {
          // 하나라도 거부한다면.
          MaterialDialog.createDialog(this) {
            title("앱 권한")
            message("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보>권한> 에서 모든 권한을 허용해 주십시오")
            positiveButton("권한설정") {
              Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:" + applicationContext.packageName)
                startActivity(this)
              }
            }
            negativeButton(getString(R.string.cancel))
          }.show()
        }
      }
    }
  }

  private fun goNext() {
    lifecycleScope.launchWhenCreated {
      delay(1500)
      startActivity(Intent(this@SplashActivity, Main2Activity::class.java))
      finish()
    }
  }

  override fun onBackPressed() {}
}
