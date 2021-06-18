package com.keelim.nandadiagnosis.utils;


import static androidx.lifecycle.Lifecycle.Event.ON_CREATE;
import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
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

import timber.log.Timber;

public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private static final String AD_REAL_ID ="ca-app-pub-3115620439518585/2318260160";
    private static boolean isShowingAd = false;
    private final MyApplication myApplication;
    private long loadTime = 0;
    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;

    public AppOpenManager(MyApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(ON_CREATE)
    public void onCreate(){
        showAdIfAvailable();
        Timber.d("onCreate");
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        showAdIfAvailable();
        Timber.d("onStart");
    }

    /** Request an ad
     * Have unused ad, no need to fetch another.
     * */
    public void fetchAd() {
        if (isAdAvailable()) {
            return;
        }

        /**
         * Called when an app open ad has loaded.
         *
         * @param ad the loaded app open ad.
         */
        /**
         * Called when an app open ad has failed to load.
         *
         * @param loadAdError the error.
         * Handle the error.
         */
        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                AppOpenManager.this.appOpenAd = ad;
                AppOpenManager.this.loadTime = (new Date()).getTime();
            }

            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             * Handle the error.
             */
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {

            }

        };
        AdRequest request = getAdRequest();
        if(BuildConfig.DEBUG){
            AppOpenAd.load(
                    myApplication, AD_UNIT_ID, request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
        } else{
            AppOpenAd.load(
                    myApplication, AD_REAL_ID, request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
        }

    }

    @NonNull
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

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

    /**
     *Only show ad if there is not already an app open ad currently showing
     *and an ad is available.
     */
    public void showAdIfAvailable() {

        if (!isShowingAd && isAdAvailable()) {
            Timber.d("Will show ad.");
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                /**
                 * Set the reference to null so isAdAvailable() returns false.
                 */
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

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);

        } else {
            Timber.d("Can not show ad.");
            fetchAd();
        }
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }
}