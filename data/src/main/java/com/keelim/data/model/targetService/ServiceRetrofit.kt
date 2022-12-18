package com.keelim.data.model.targetService

import retrofit2.Retrofit

sealed class  ServiceRetrofit(val retrofit: Retrofit) {
    class CnuBusRetrofit(retrofit: Retrofit): ServiceRetrofit(retrofit)
    class MyGradeRetrofit(retrofit: Retrofit): ServiceRetrofit(retrofit)
    class ComssaRetrofit(retrofit: Retrofit): ServiceRetrofit(retrofit)
    class NandaRetrofit(retrofit: Retrofit): ServiceRetrofit(retrofit)
    class YrRetrofit(retrofit: Retrofit): ServiceRetrofit(retrofit)
}
