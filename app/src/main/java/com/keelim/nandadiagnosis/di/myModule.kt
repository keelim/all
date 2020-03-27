package com.keelim.nandadiagnosis.di

import okhttp3.Request
import org.koin.dsl.module


var list = module {
    single {
        Request.Builder()
                .url("https://github.com/keelim/Keelim.github.io/raw/master/assets/nanda.db")
                .build()
    }
}


var myDiModule = listOf(list)