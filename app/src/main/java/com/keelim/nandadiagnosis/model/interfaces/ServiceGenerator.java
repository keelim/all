package com.keelim.nandadiagnosis.model.interfaces;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ServiceGenerator {

    public static final String API_BASE_URL = "/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL);

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

}