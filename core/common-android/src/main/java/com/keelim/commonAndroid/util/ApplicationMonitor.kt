package com.keelim.commonAndroid.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.keelim.commonAndroid.BuildConfig
import com.keelim.commonAndroid.model.AppInfo
import kotlinx.datetime.Clock
import timber.log.Timber
import javax.inject.Inject

class ApplicationMonitor @Inject constructor(
    private val appInfo: AppInfo,
) : LifecycleObserver {
    private val adRequest: AdRequest by lazy { AdRequest.Builder().build() }
    private var appOpenAd: AppOpenAd? = null
    private var application: Application? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private var loadTime: Long = 0
    private val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo()

    fun initialize(application: Application) {
        this.application = application

        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    Timber.d("${activity::class.java.simpleName} onCreated")
                }

                override fun onActivityStarted(activity: Activity) {
                    currentActivity = activity
                    showAdIfAvailable()
                    Timber.d("${activity::class.java.simpleName} Started")
                }

                override fun onActivityResumed(activity: Activity) {
                    currentActivity = activity
                    Timber.d("${activity::class.java.simpleName} Resumed")
                }

                override fun onActivityStopped(activity: Activity) {
                    Timber.d("${activity::class.java.simpleName} Stopped")
                }

                override fun onActivityPaused(activity: Activity) {
                    Timber.d("${activity::class.java.simpleName} onPaused")
                }

                override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
                    Timber.d("${activity::class.java.simpleName} onSaveInstanceState")
                }

                override fun onActivityDestroyed(activity: Activity) {
                    currentActivity = null
                    Timber.d("${activity::class.java.simpleName} onDestroyed")
                }
            },
        )
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /** Request an ad Have unused ad, no need to fetch another. */
    fun fetchAd() {
        if (isAdAvailable) {
            return
        }
        /**
         * Called when an app open ad has loaded.
         *
         * @param ad the loaded app open ad.
         */
        /**
         * Called when an app open ad has failed to load.
         *
         * @param loadAdError the error. Handle the error.
         */
        val loadCallback: AppOpenAd.AppOpenAdLoadCallback =
            object : AppOpenAd.AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    loadTime = Clock.System.now().epochSeconds
                }

                /**
                 * Called when an app open ad has failed to load.
                 *
                 * @param loadAdError the error. Handle the error.
                 */
                override fun onAdFailedToLoad(loadAdError: LoadAdError) = Unit
            }
        AppOpenAd.load(
            application ?: return,
            if (BuildConfig.DEBUG) {
                AD_UNIT_ID
            } else {
                if (appInfo.adId.isEmpty()) return
                appInfo.adId
            },
            adRequest,
            loadCallback,
        )
    }

    /**
     * Only show ad if there is not already an app open ad currently showing and an ad is available.
     */
    fun showAdIfAvailable() {
        takeIf { !isShowingAd && isAdAvailable }
            ?.run {
                Timber.d("Will show ad.")
                appOpenAd?.apply {
                    fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            /** Set the reference to null so isAdAvailable() returns false. */
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
            } ?: run {
            Timber.d("Can not show ad.")
            fetchAd()
        }
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 4): Boolean {
        val dateDifference = Clock.System.now().epochSeconds - loadTime
        val numMilliSecondsPerHour = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    companion object {
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"
    }
}
