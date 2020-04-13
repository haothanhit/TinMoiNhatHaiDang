package com.haidang.tinmoinhat.common.retrofit

import com.google.gson.GsonBuilder
import com.haidang.tinmoinhat.common.global.Constants.Companion.URL_CATE_CHINH
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APIClient {


    private val retrofit = Retrofit.Builder()
        .baseUrl(URL_CATE_CHINH)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun <T> getClient(service: Class<T>): T {
        return retrofit.create(service)
    }
}