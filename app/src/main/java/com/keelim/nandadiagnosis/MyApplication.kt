package com.keelim.nandadiagnosis

import android.app.Application
import com.keelim.nandadiagnosis.di.myDiModule
import org.koin.android.ext.koin.androidContext

import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(myDiModule)
        }

    }
}