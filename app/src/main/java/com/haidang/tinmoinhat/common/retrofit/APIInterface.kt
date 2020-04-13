package com.haidang.tinmoinhat.common.retrofit

import com.haidang.tinmoinhat.common.global.Constants.Companion.URL_CATE_CHINH1
import com.haidang.tinmoinhat.common.global.Constants.Companion.URL_DATA_CHINH1
import com.haidang.tinmoinhat.common.model.ModelArticle
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET(URL_CATE_CHINH1)
    fun getArticle(
        @Query("catid") catid: String,
        @Query("page") page: String
    ): Observable<ArrayList<ModelArticle>>

    @GET(URL_DATA_CHINH1)
    fun getNews(@Query("arcid") arcid: String): Observable<String>
}