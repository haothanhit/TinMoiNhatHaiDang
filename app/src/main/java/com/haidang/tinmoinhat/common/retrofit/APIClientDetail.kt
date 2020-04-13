package com.haidang.tinmoinhat.common.retrofit

import com.haidang.tinmoinhat.common.global.Constants
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object APIClientDetail {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL_DATA_CHINH)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun <T> getClient(service: Class<T>): T {
        return retrofit.create(service)
    }
}