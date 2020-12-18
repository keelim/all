package com.keelim.nandadiagnosis

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.keelim.nandadiagnosis.error.ExceptionHandler
import com.keelim.nandadiagnosis.module.downloadModule
import com.keelim.nandadiagnosis.utils.AppOpenManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    private lateinit var appOpenManager: AppOpenManager
    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this) {}
        appOpenManager = AppOpenManager(this) // 콜드 부팅에서 복귀시 ad

        setCrashHandler()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(downloadModule)
        }

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