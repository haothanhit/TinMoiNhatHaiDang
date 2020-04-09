package com.haidang.tinmoinhat.common.retrofit

import com.haidang.tinmoinhat.common.global.Constants.Companion.URL_CATE_CHINH1
import com.haidang.tinmoinhat.common.model.ModelArticle
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET(URL_CATE_CHINH1)
    fun getNews(@Query("catid") catid: String, @Query("page") page: String): Call<ArrayList<ModelArticle>>
}