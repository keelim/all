package com.keelim.nandadiagnosis.utils;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.keelim.nandadiagnosis.BuildConfig;
import com.keelim.nandadiagnosis.MyApplication;

import java.util.Date;

/**
 * Prefetches App Open Ads.
 */
public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenManager";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private static boolean isShowingAd = false;
    private AppOpenAd appOpenAd = null;

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private final MyApplication myApplication;
    private Activity currentActivity;

    private long loadTime = 0;

    /**
     * Constructor
     */
    public AppOpenManager(MyApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAppOpenAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error
                     */
                    @Override
                    public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                    }

                };
        AdRequest request = getAdRequest();
//        AppOpenAd.load(myApplication, myApplication.getString(R.string.ad_unit1), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
//        AppOpenAd.load(myApplication, AD_UNIT_ID, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback); //test unit ID

        if (BuildConfig.DEBUG)
            AppOpenAd.load(myApplication, AD_UNIT_ID, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback); //test unit ID
        else
            AppOpenAd.load(myApplication, "ca-app-pub-3115620439518585/2318260160", request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback); //test unit ID

    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

    public void showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "will show ad");

            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    AppOpenManager.this.appOpenAd = null;
                    isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                }
            };

            appOpenAd.show(currentActivity, fullScreenContentCallback);
        } else {
            Log.d(LOG_TAG, "Can not show ad. ");
            fetchAd();
        }
    }

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

}