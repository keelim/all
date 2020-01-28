package com.keelim.cnubus.di

import org.koin.dsl.module


var room = module{

}

//var retrofit = module{
//    single<KakaoSearchService>{
//        Retrofit.Builder()
//            .baseUrl("https://dapi.kakao.com")
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(KakaoSearchService::class.java)
//    }
//}
//
//var adapterPart = module{
//    factory {
//        MainSearchRecyclerViewAdapter()
//    }
//}
//
//var modelPart = module{
//    factory<DataModel>{
//        DataModelImpl(get())
//
//    }
//}
//
//var viewModelPart = module{
//    viewModel{
//        MainViewModel(get())
//    }
//}

var myDiModule = listOf(room)