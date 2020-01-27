package com.keelim.nandadiagnosis

import android.app.Application
import com.keelim.nandadiagnosis.di.myDiModule
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, myDiModule)
    }
}