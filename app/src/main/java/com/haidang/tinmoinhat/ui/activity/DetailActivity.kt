package com.haidang.tinmoinhat.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.adapter.AdapterDetail
import com.haidang.tinmoinhat.common.adapter.AdapterRelate
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_RECENT_READING
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_RELATE
import com.haidang.tinmoinhat.common.model.ModelArticle
import com.haidang.tinmoinhat.common.model.ModelContent
import com.haidang.tinmoinhat.common.retrofit.APIClientDetail
import com.haidang.tinmoinhat.common.retrofit.APIInterface
import kotlinx.android.synthetic.main.activity_detail.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : BaseActivity() {
    private val TAG: String = DetailActivity::class.java.simpleName
    private var data: ModelArticle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        data = intent.getSerializableExtra("Article") as ModelArticle?
        initView()
    }

    @SuppressLint("WrongConstant")
    private fun initView() {
        //AdView
        val adRequest = AdRequest.Builder().build()
        adView_detail.loadAd(adRequest)
        //Layout detail
        var linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_view_details.layoutManager = linearLayoutManager
        recycler_view_details.setHasFixedSize(true)
        getData()

        //Layout relate
        recycler_view_relate.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_view_relate.setHasFixedSize(true)
        val dividerHorizontal =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        recycler_view_relate.addItemDecoration(dividerHorizontal)
        loadRelate()

        //save RecentReading
        val appSharedPrefs: SharedPreferences =
            this.getSharedPreferences(KEY_RECENT_READING, Context.MODE_PRIVATE)
        val prefsEditor = appSharedPrefs.edit()
        val typeRecent = object : TypeToken<ArrayList<ModelArticle?>?>() {}.type
        val jsonSave: String = appSharedPrefs.getString(KEY_RECENT_READING, "").toString()
        var arrayListSave = ArrayList<ModelArticle>()
        var isSaved = true
        if (jsonSave != "") {
            try {
                arrayListSave = Gson().fromJson(jsonSave, typeRecent)

            } catch (ex: java.lang.Exception) {
                var a: ModelArticle =
                    Gson().fromJson(jsonSave, object : TypeToken<ModelArticle>() {}.type)
                arrayListSave.add(a)
            }
            for (i in arrayListSave) {
                if (i.id.equals(data?.id)) {
                    isSaved = false
                    break
                }
            }
            arrayListSave.add(data!!)
        } else {
            arrayListSave.add(data!!)
        }
        if (isSaved) {   // if article not saved
            val json1 = Gson().toJson(arrayListSave)
            prefsEditor.putString(KEY_RECENT_READING, json1).apply()
        }

    }

    private fun loadRelate() {
        val gson = Gson()
        val json =getSharedPrefsString(KEY_RELATE)
        val type = object : TypeToken<ArrayList<ModelArticle>>() {}.type
        if (json != "") {

            //Layout relate
            val arrayList = ArrayList<ModelArticle>()
            var arrayListRelate = ArrayList<ModelArticle>()
            try {
                arrayListRelate = gson.fromJson(json, type)

            } catch (ex: Exception) {
                var a: ModelArticle =
                    gson.fromJson(json, object : TypeToken<ModelArticle>() {}.type)
                arrayListRelate.add(a)
            }
            if (arrayListRelate.size > 1) {
                var i = 0
                while (arrayList.size < 7) {  //tong so bai viet lien quan se hien
                    val ran = Random()
                    val pos = ran.nextInt(arrayListRelate.size - 1)
                    if (!arrayListRelate.get(pos).id.equals(data?.id)) { //bo show bai viet hien tai
                        arrayList.add(arrayListRelate.get(pos))
                        arrayListRelate.removeAt(pos)
                    }
                    i++
                }
            }
            recycler_view_relate.adapter = AdapterRelate(arrayList, this)

        }


    }

    fun getData() {
        val request = APIClientDetail.getClient(APIInterface::class.java)
        val call = request.getNews(data?.id!!)
        Log.d(TAG, call.request().url.toString())

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful && response.body() != null) {

                    var arrayList = ArrayList<ModelContent>()
                    val document: org.jsoup.nodes.Document = Jsoup.parse(response.body()!!)
                    val element: Element = document.select("div.content-detail").first()

                    if (element != null) {
                        val elements: Elements = element.select("p")
                        val title: String = document.select("h3.title").text()
                        val source: String = document.select("p").first().text()
                        arrayList.add(ModelContent(title, ""))
                        arrayList.add(ModelContent(source, ""))

                        //API mới
                        val shot: String
                        if (document.select("p.title").first() != null) {
                            shot = document.select("p.title").first().text()
                            arrayList.add(ModelContent(shot, ""))
                        }
                        for (e in elements) {
                            var text = ""
                            var img = ""
                            if (e.getElementsByTag("img").first() != null) {
                                img = e.getElementsByTag("img").first().attr("src")
                            }
                            if (img == "") {
                                text = e.text()
                            }
                            arrayList.add(ModelContent(text, img))
                        }
                    }


                    recycler_view_details.adapter = AdapterDetail(arrayList)
                    frame_item.visibility = View.VISIBLE

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, t.message)
            }
        })
    }
}