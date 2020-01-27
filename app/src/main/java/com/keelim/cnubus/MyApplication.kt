package com.keelim.cnubus

import android.app.Application
import com.keelim.cnubus.di.myDiModule
import org.koin.android.ext.android.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, myDiModule)
    }
}