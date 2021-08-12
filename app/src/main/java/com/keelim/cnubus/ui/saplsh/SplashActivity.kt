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
package com.keelim.cnubus.ui.saplsh

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.databinding.ActivitySplashBinding
import com.keelim.cnubus.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private var mInterstitialAd: InterstitialAd? = null
    private val test = "ca -app-pub-3940256099942544/1033173712"
    private infix fun String.or(that: String): String = if (BuildConfig.DEBUG) this else that
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeData()
    }

    private fun observeData() = lifecycleScope.launchWhenCreated{
        splashViewModel.loading.collect {
            if(it){
                showAd()
            }
        }
    }

    private fun showAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            test or "ca-app-pub-3115620439518585/4013096159",
            adRequest,
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
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {}
}
