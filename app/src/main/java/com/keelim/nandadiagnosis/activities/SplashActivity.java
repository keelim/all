package com.keelim.nandadiagnosis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.keelim.nandadiagnosis.R;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity { //handler를 다르게 설정을 할 수 있는가?
    //인트로 액티비티를 생성한다.
    private Handler handler;
    private InterstitialAd interstitialAd;
    //인앱 업데이트 어디서 등록을 해야 하는가?
    private Runnable runnable = () -> { //runable 작동을 하고 시작
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); //인텐트를 넣어준다. intro -> main
        finish(); //앱을 종료한다.
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //애니메이션을 넣어준다.
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics()); //Fabric 설정
        setContentView(R.layout.activity_splash);
        Toast.makeText(this, "NANDA 진단에 오신 것을 환영 합니다. ", Toast.LENGTH_SHORT).show();
        MobileAds.initialize(this, initializationStatus -> {
        });

        interstitialAd = new InterstitialAd(this); //전면광고 셋팅
        interstitialAd.setAdUnitId(getString(R.string.test_ad));
        interstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdLoaded() {
                Toast.makeText(SplashActivity.this, "Loading complete", Toast.LENGTH_SHORT).show();
                interstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                handler = new Handler();
                handler.postDelayed(runnable, 500); //handler를 통하여 사용
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Error", "ad loading fail");
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() { //back 키 눌렀을 때
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}
