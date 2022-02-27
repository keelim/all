package com.keelim.cnubus.data.network

import okhttp3.Request
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ResultOption<T>(val delegate: Call<T>) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(
            object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if(response.isSuccessful){
                        callback.onResponse(
                            this@ResultOption,
                            Response.success(
                                response.code(),
                                Result.success(response.body()!!)
                            )
                        )
                    } else{
                        callback.onResponse(
                            this@ResultOption,
                            Response.success(
                                Result.failure(
                                    HttpException(response)
                                )
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    val errorMessage = when(t){
                        is IOException -> "No Network Connection"
                        is HttpException -> "some problem in http method"
                        else -> t.localizedMessage
                    }

                    callback.onResponse(
                        this@ResultOption,
                        Response.success(Result.failure(RuntimeException(errorMessage, t)))
                    )
                }

            }
        )
    }
    override fun clone(): Call<Result<T>> = ResultOption(delegate.clone())

    override fun execute(): Response<Result<T>> = Response.success(Result.success(delegate.execute().body()!!))

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}