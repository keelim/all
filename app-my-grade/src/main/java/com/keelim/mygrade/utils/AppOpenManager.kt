package com.keelim.mygrade.utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.keelim.mygrade.BuildConfig
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class AppOpenManager @Inject constructor() : LifecycleObserver {
    private val adRequest: AdRequest by lazy { AdRequest.Builder().build() }

    private var isShowingAd = false
    private var loadTime: Long = 0

    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var application: Application? = null
    private val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo()

    fun initialize(application: Application) {
        this.application = application

        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
                showAdIfAvailable()
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                currentActivity = null
            }
        })
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * Request an ad
     * Have unused ad, no need to fetch another.
     */
    fun fetchAd() {
        if (isAdAvailable) {
            return
        }
        /**
         * Called when an app open ad has loaded.
         * @param ad the loaded app open ad.
         */
        /**
         * Called when an app open ad has failed to load.
         * @param loadAdError the error.
         * Handle the error.
         */
        val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             * @param ad the loaded app open ad.
             */
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                loadTime = Date().time
            }

            /**
             * Called when an app open ad has failed to load.
             * @param loadAdError the error.
             * Handle the error.
             */
            override fun onAdFailedToLoad(loadAdError: LoadAdError) = Unit
        }
        AppOpenAd.load(
            application ?: return,
            if (BuildConfig.DEBUG) {
                AD_UNIT_ID
            } else {
                BuildConfig.AD_OPEN_ID
            },
            adRequest,
            loadCallback
        )
    }

    /**
     * Only show ad if there is not already an app open ad currently showing
     * and an ad is available.
     */
    fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable) {
            Timber.d("Will show ad.")
            appOpenAd!!.apply {
                fullScreenContentCallback = object : FullScreenContentCallback() {
                    /**
                     * Set the reference to null so isAdAvailable() returns false.
                     */
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
                currentActivity?.also(::show)
            }
        } else {
            Timber.d("Can not show ad.")
            fetchAd()
        }
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 4): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    companion object {
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"
    }
}
