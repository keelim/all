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
package com.keelim.comssa.ui


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.keelim.comssa.BuildConfig
import com.keelim.comssa.R
import com.keelim.comssa.databinding.ActivitySplashBinding
import com.keelim.comssa.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
  private var mInterstitialAd: InterstitialAd? = null
  private val binding by lazy {ActivitySplashBinding.inflate(layoutInflater)}

  private val test = "ca-app-pub-3940256099942544/1033173712"

  private infix fun String.or(that: String): String = if (BuildConfig.DEBUG) this else that
  private val scope = MainScope()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initSplash()
  }

  private fun initSplash() {
    val permissions = arrayOf(
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.READ_EXTERNAL_STORAGE
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      permissions.plus(Manifest.permission.FOREGROUND_SERVICE)
    }
    if (hasPermissions(permissions)) { // 권한이 있는 경우
      showAd()
    } else {
      ActivityCompat.requestPermissions(this, permissions, MULTIPLE_PERMISSIONS)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    scope.cancel()
  }

  private fun showAd() {
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(this,
      test or "ca-app-pub-3115620439518585/4013096159",
      adRequest,
      object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
          super.onAdFailedToLoad(adError)
          Timber.d(adError.message)
          mInterstitialAd = null
          goNext()
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
          super.onAdLoaded(interstitialAd)
          Timber.d("Ad was loaded.")
          mInterstitialAd = interstitialAd
          mInterstitialAd!!.show(this@SplashActivity)
          goNext()
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
          Snackbar.make(binding.root, "모든 권한이 승인 되었습니다.", Snackbar.LENGTH_SHORT).show()
          initSplash()
        }
      }
    }
  }

  private fun goNext() {
    scope.launch {
      delay(1500)
      startActivity(Intent(this@SplashActivity, MainActivity::class.java))
      finish()
    }
  }

  override fun onBackPressed() {}

  companion object {
    const val MULTIPLE_PERMISSIONS = 8888
  }
}
