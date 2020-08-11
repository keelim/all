package com.keelim.nandadiagnosis.model.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface API {

    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String s);
}

