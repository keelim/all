package com.keelim.cnubus.utils

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
import com.keelim.cnubus.BuildConfig
import java.util.Date
import javax.inject.Inject
import timber.log.Timber

class AppOpenManager @Inject constructor() : LifecycleObserver {
    private lateinit var myApplication: Application
    private var loadTime: Long = 0
    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private val adRequest by lazy { AdRequest.Builder().build() }
    private val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo()

    fun initialize(application: Application) {
        this.myApplication = application
        this.myApplication.registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
                showAdIfAvailable()
                Timber.d("onStart")
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

    fun fetchAd() {
        if (isAdAvailable) {
            return
        }
        AppOpenAd.load(
            myApplication,
            if(BuildConfig.DEBUG){
                AD_UNIT_ID
            } else{
                BuildConfig.UNIT
            },
            adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    loadTime = Date().time
                }
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
            }
        )
    }

    private fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable) {
            Timber.d("Will show ad.")
            appOpenAd!!.setFullScreenContentCallback(object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    fetchAd()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            })
            if(appOpenAd!= null && currentActivity != null){
                appOpenAd!!.show(currentActivity!!)
            }
        } else {
            Timber.d("Can not show ad.")
            fetchAd()
        }
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 4): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    companion object {
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"
    }
}