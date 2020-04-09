package com.haidang.tinmoinhat.common.retrofit

import com.haidang.tinmoinhat.common.global.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object APIClientDetail {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL_DATA_CHINH)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(client)
        .build()

    fun <T> getClient(service: Class<T>): T {
        return retrofit.create(service)
    }
}