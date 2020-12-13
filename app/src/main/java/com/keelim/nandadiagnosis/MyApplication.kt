package com.keelim.nandadiagnosis

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.keelim.nandadiagnosis.error.ExceptionHandler
import com.keelim.nandadiagnosis.util.AppOpenManager

class MyApplication : Application() {
    private lateinit var appOpenManager: AppOpenManager
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        appOpenManager = AppOpenManager(this) // 콜드 부팅에서 복귀시 ad

        setCrashHandler()
    }

    private fun setCrashHandler() {

        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()!!
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            // Crashlytics에서 기본 handler를 호출하기 때문에 이중으로 호출되는것을 막기위해 빈 handler로 설정
        }

        val fabricExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()!!
        Thread.setDefaultUncaughtExceptionHandler(
                ExceptionHandler(
                        this,
                        defaultExceptionHandler,
                        fabricExceptionHandler
                )
        )
    }
}