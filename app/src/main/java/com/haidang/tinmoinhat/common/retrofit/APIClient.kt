package com.haidang.tinmoinhat.common.retrofit

import com.haidang.tinmoinhat.common.global.Constants.Companion.URL_CATE_CHINH
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  APIClient {
    private var client = OkHttpClient.Builder().retryOnConnectionFailure(true).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL_CATE_CHINH)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> getClient(service: Class<T>): T{
        return retrofit.create(service)
    }
}