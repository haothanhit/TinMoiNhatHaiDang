package com.haidang.tinmoinhat.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.adapter.AdapterTickAndSaveArticle
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_RECENT_READING
import com.haidang.tinmoinhat.common.model.ModelArticle
import kotlinx.android.synthetic.main.activity_tick_and_save.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class TickAndSaveActivity :BaseActivity() {
  private  var tickOrSave: String?="" // recentReading   or
    var arrayList =ArrayList<ModelArticle>()
    private var mAdaper:AdapterTickAndSaveArticle?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tick_and_save)
        tickOrSave = intent.getStringExtra("TickOrSave")
        initView()
    }

    @SuppressLint("WrongConstant")
    private fun initView() {
        //Layout relate
        recycler_view_tick_or_save.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_view_tick_or_save.setHasFixedSize(true)
        val dividerHorizontal =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        recycler_view_tick_or_save.addItemDecoration(dividerHorizontal)
       var json= getSharedPrefsString(tickOrSave!!)
        val type = object : TypeToken<ArrayList<ModelArticle>>() {}.type
        if (json != "") {
            try {
                arrayList = Gson().fromJson(json, type)

            } catch (ex: Exception) {
                var a: ModelArticle =
                    Gson().fromJson(json, object : TypeToken<ModelArticle>() {}.type)
                arrayList.add(a)
            }
            arrayList.reverse()
            mAdaper= AdapterTickAndSaveArticle(arrayList, this,tickOrSave!!)
            recycler_view_tick_or_save.adapter =mAdaper

        }
        titleTickSave.text=if(tickOrSave.equals(KEY_RECENT_READING)) getString(R.string.tin_doc_gan_day)else getString(R.string.tin_da_luu)

        xoa_tat_ca_save.setOnClickListener {
            clearSharedPrefs(tickOrSave!!)
            mAdaper?.clearData()
        }

        ivBackTickSave.setOnClickListener { onBackPressed() }
    }

}